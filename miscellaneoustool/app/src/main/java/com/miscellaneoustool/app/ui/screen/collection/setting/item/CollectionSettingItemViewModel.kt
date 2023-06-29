package com.miscellaneoustool.app.ui.screen.collection.setting.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miscellaneoustool.domain.usecase.collection.GetItemsUsecase
import com.miscellaneoustool.domain.usecase.collection.UpdateItemPriceUsecase
import com.miscellaneoustool.app.ui.screen.collection.item.item.CollectionItemState
import com.miscellaneoustool.app.ui.screen.collection.mapper.toCollectionItemState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingUiState(
    val items: List<CollectionItemState>
) {
    companion object {
        fun getInitValues() = SettingUiState(emptyList())
    }
}

@HiltViewModel
class CollectionSettingITemViewModel @Inject constructor(
    private val getItemsUsecase: com.miscellaneoustool.domain.usecase.collection.GetItemsUsecase,
    private val updateItemPriceUsecase: com.miscellaneoustool.domain.usecase.collection.UpdateItemPriceUsecase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingUiState.getInitValues())
    val uiState get() = _uiState.asStateFlow()

    init {
        getItems()
    }

    private fun getItems() =
        getItemsUsecase().onEach { itemModels ->
            _uiState.update { state ->
                state.copy(items = itemModels.map { itemModel ->
                    itemModel.toCollectionItemState()
                })
            }
        }.catch {

        }.launchIn(viewModelScope)

    fun updateItemPrice(name: String, price: Int) {
        viewModelScope.launch {
            updateItemPriceUsecase(name = name, price = price)
        }
    }
}