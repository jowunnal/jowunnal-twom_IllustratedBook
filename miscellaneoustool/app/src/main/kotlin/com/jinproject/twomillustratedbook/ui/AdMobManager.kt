package com.jinproject.twomillustratedbook.ui

import android.app.Activity
import com.google.android.gms.ads.MobileAds
import com.jinproject.twomillustratedbook.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AdMobManager(
    private val activity:Activity,
) {
    private var _isAdViewRemoved = MutableStateFlow(true)
    val isAdviewRemoved get() = _isAdViewRemoved.asStateFlow()

    fun initAdView() {
        if(!BuildConfig.IS_DEBUG_MODE) {
            MobileAds.initialize(activity) { }
            updateIsAdViewRemoved(false)
        }
    }

    fun updateIsAdViewRemoved(boolean: Boolean) = _isAdViewRemoved.update { boolean }
}