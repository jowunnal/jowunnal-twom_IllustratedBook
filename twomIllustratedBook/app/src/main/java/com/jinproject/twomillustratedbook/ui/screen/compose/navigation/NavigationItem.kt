package com.jinproject.twomillustratedbook.ui.screen.compose.navigation

import androidx.compose.runtime.Stable

@Stable
sealed class NavigationItem(
    val title:String,
    val route:String
) {
    object Alarm: NavigationItem(
        title = "알람",
        route = "alarm"
    )
}