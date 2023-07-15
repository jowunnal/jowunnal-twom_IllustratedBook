package com.jinproject.twomillustratedbook.ui.screen.watch.item

import androidx.compose.runtime.Stable

@Stable
enum class ButtonStatus(val displayName: String) {
    ON(displayName = "ON"),
    OFF(displayName = "OFF");

    operator fun not() = when(this) {
        ON -> OFF
        OFF -> ON
    }
}
