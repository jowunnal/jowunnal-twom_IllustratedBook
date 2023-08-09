package com.jinproject.features.core

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.LifecycleCoroutineScope
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchaseHistoryParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.queryProductDetails
import com.android.billingclient.api.queryPurchaseHistory
import com.jinproject.features.core.BillingModule.Product.Companion.findProductById
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 결제 관리 모듈 클래스
 * @param context 결제 객체생성에 필요
 * @param lifeCycleScope 수행될 코루틴 스쿠프
 * @param callback 결제 수행 콜백
 * @property purchasableProducts 구입 가능한 상품들
 * @property isReady 결제를 수행하기에 준비된 상태인지의 여부
 */
@Stable
class BillingModule(
    private val context: Activity,
    private val lifeCycleScope: LifecycleCoroutineScope,
    private val callback: BillingCallback
) {

    val purchasableProducts = ArrayList<ProductDetails>()
    var isReady: Boolean = false

    private val purChasedUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                if (purchases != null) {
                    for (purchase in purchases) {
                        lifeCycleScope.launch(Dispatchers.IO) {
                            handlePurchase(
                                purchase,
                                purchase.products.first().findProductById()!!.isConsume
                            )
                        }
                    }
                }
            }

            else -> {
                callback.onFailure(billingResult.responseCode)
            }
        }
    }

    private var billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(purChasedUpdatedListener)
        .enablePendingPurchases()
        .build()

    init {
        initBillingClient(3)
    }

    fun initBillingClient(maxCount: Int) {
        if (maxCount > 0) {
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        callback.onReady()
                        isReady = true
                    } else {
                        callback.onFailure(billingResult.responseCode)
                    }
                }

                override fun onBillingServiceDisconnected() {
                    initBillingClient(maxCount - 1)
                    Log.e("test", "disconnected")
                }
            })
        }
    }

    /**
     * 구매 가능한 상품 목록을 조회
     * @param initAdView 광고제거 를 구매하지 않은 경우, 광고를 게재 콜백
     */
    suspend fun getPurchasableProducts(
        initAdView: () -> Unit
    ) {
        val productList = ArrayList<QueryProductDetailsParams.Product>().apply {
            Product.values().forEach { product ->
                add(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(product.id)
                        .setProductType(product.type)
                        .build()
                )
            }
        }

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        val productDetailsResult = withContext(Dispatchers.IO) {
            billingClient.queryProductDetails(params)
        }

        if (productDetailsResult.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {

            queryPurchase { purchaseList ->

                val totalProducts =
                    productDetailsResult.productDetailsList?.filter { productDetail ->
                        productDetail.productId.findProductById() != null
                    } ?: emptyList()

                val purchasedProductIds = purchaseList.distinctBy {
                    it.products
                }.map {
                    it.products.first()
                }

                //Log.d("test",purchasedProductIds.map{it.findProductById()}.toString())

                val purchasableProducts = totalProducts.filter { purchasableProduct ->
                    purchasableProduct.productId !in purchasedProductIds
                }

                this.purchasableProducts.addAll(
                    purchasableProducts
                )

                if (checkPurchased(
                        purchaseList = purchaseList,
                        productId = "ad_remove"
                    )
                )
                    withContext(Dispatchers.Main) {
                        initAdView()
                    }
            }
        }
    }

    /**
     * 구매 수행
     * @param productDetails : 구매 가능한 상품
     */

    fun purchase(productDetails: ProductDetails) {
        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        billingClient.launchBillingFlow(context, billingFlowParams)
    }

    /**
     * 구매 처리
     * @param purchase 구매 상품
     * @param isConsume 소비 여부
     */
    private suspend fun handlePurchase(purchase: Purchase, isConsume: Boolean) {
        when (isConsume) {
            true -> {
                val consumeParams =
                    ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()
                withContext(Dispatchers.IO) {
                    billingClient.consumeAsync(consumeParams) { result, token ->
                        if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                            callback.onSuccess(purchase)
                        } else {
                            callback.onFailure(result.responseCode)
                        }
                    }
                }
            }

            false -> {
                if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
                    val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)

                    withContext(Dispatchers.IO) {
                        billingClient.acknowledgePurchase(acknowledgePurchaseParams.build()) { billingResult ->
                            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                                callback.onSuccess(purchase)
                            } else {
                                callback.onFailure(billingResult.responseCode)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 구매 기록 조회
     * @param doOnPurchaseList 구매 기록에 따라 처리할 콜백
     */
    fun queryPurchase(doOnPurchaseList: suspend (List<Purchase>) -> Unit) {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        billingClient.queryPurchasesAsync(params) { billingResult, purchaseList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                lifeCycleScope.launch {
                    doOnPurchaseList(purchaseList)
                }
            }
        }
    }

    /**
     * 결제 재확인(승인)
     * @param purchaseList 구매 목록
     */
    suspend fun approvePurchased(purchaseList: List<Purchase>) {
        purchaseList.forEach { purchase ->
            if (!purchase.isAcknowledged && purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                handlePurchase(
                    purchase = purchase,
                    purchase.products.first().findProductById()!!.isConsume
                )
            }
        }
    }

    /**
     * 구매 목록의 상품들이 구매했는지의 여부를 확인
     * @param purchaseList 구매 목록
     * @param productId 비교할 상품 id
     */
    fun checkPurchased(purchaseList: List<Purchase>, productId: String): Boolean =
        purchaseList.none { purchase ->
            (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) && purchase.products.contains(
                productId
            )
        }


    suspend fun queryPurchaseLatest() {
        val params = QueryPurchaseHistoryParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)

        return withContext(Dispatchers.IO) { billingClient.queryPurchaseHistory(params.build()) }
    }

    interface BillingCallback {
        fun onReady()
        fun onSuccess(purchase: Purchase)
        fun onFailure(errorCode: Int)
    }

    enum class Product(val id: String, val type: String, val isConsume: Boolean) {
        AD_REMOVE("ad_remove", BillingClient.ProductType.INAPP, false),
        SUPPORT("support", BillingClient.ProductType.INAPP, true);

        companion object {
            fun String.findProductById() =
                kotlin.runCatching { values().find { value -> value.id == this } }.getOrNull()
        }
    }
}