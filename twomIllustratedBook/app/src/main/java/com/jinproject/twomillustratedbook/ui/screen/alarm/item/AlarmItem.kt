package com.jinproject.twomillustratedbook.ui.screen.alarm.item

import androidx.compose.runtime.Stable

@Stable
data class AlarmItem(
    val name: String,
    val imgName: String,
    val code: Int,
    val gtime: Int
) {
    companion object {
        fun getInitValue() = AlarmItem(
            name = "",
            imgName = "",
            code = -1,
            gtime = -1
        )
    }
}