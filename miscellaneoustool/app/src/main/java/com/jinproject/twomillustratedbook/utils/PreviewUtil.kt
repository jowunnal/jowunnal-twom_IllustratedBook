package com.jinproject.twomillustratedbook.utils

import androidx.compose.runtime.Composable
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.MiscellaneousToolTheme

@Composable
fun TwomIllustratedBookPreview(content: @Composable () -> Unit) {
    MiscellaneousToolTheme() { content() }
}