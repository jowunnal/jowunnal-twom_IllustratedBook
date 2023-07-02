package com.jinproject.twomillustratedbook.ui.screen.watch.component

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.chargemap.compose.numberpicker.NumberPicker
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.MiscellaneousToolTheme
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.Typography

@Composable
fun OverlaySetting(
    fontSize: Int,
    xPos: Int,
    yPos: Int,
    setFontSize: (Int) -> Unit,
    setXPos: (Int) -> Unit,
    setYPos: (Int) -> Unit,
    context: Context = LocalContext.current
) {
    val displayMetrics = context.applicationContext.resources.displayMetrics
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NumberPicker(
            title = "폰트 크기 조절",
            number = fontSize,
            setNumber = setFontSize,
            range = 1..32
        )
        Spacer(modifier = Modifier.weight(1f))
        NumberPicker(
            title = "가로축 이동",
            number = xPos,
            setNumber = setXPos,
            range = -(displayMetrics.widthPixels / 2)..(displayMetrics.widthPixels / 2) step 5
        )
        Spacer(modifier = Modifier.weight(1f))
        NumberPicker(
            title = "세로축 이동",
            number = yPos,
            setNumber = setYPos,
            range = 0..displayMetrics.heightPixels / 2 step 5
        )
    }
}

@Composable
private fun NumberPicker(
    title: String,
    number: Int,
    range: IntProgression,
    setNumber: (Int) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = Typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
        NumberPicker(
            value = number,
            onValueChange = { value -> setNumber(value) },
            range = range,
            textStyle = TextStyle(color = MaterialTheme.colorScheme.outline),
            dividersColor = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
@Preview
private fun PreviewTimerPositionSetting() =
    MiscellaneousToolTheme {
        OverlaySetting(
            fontSize = 0,
            xPos = 0,
            yPos = 0,
            setFontSize = {},
            setXPos = {},
            setYPos = {}
        )
    }