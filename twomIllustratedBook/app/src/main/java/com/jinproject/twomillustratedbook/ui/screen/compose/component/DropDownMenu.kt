package com.mate.carpool.ui.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.ui.screen.compose.component.HorizontalSpacer
import com.jinproject.twomillustratedbook.ui.screen.compose.component.VerticalSpacer
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.black
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.gray
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.white
import com.jinproject.twomillustratedbook.utils.TwomIllustratedBookPreview
import com.jinproject.twomillustratedbook.utils.tu

@Composable
fun DropDownMenuCustom(
    @DrawableRes iconHeader: Int? = null,
    @DrawableRes iconTail: Int? = null,
    label: String,
    text: String,
    items: List<String>,
    setTextChanged: (String) -> Unit
) {
    val dropDownExpandedState = remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .clickable {
                dropDownExpandedState.value = !dropDownExpandedState.value
            }
    ) {
        Text(
            text = label,
            color = black,
            fontWeight = FontWeight.Normal,
            fontSize = 13.tu
        )
        VerticalSpacer(height = 1.dp)
        Row(
            modifier = Modifier
                .border(1.dp, gray, RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp, vertical = 10.dp)
        ) {
            iconHeader?.let {
                Icon(
                    painter = painterResource(id = iconHeader),
                    contentDescription = "DropDownMenuIconHeader"
                )
                HorizontalSpacer(width = 12.dp)
            }

            Text(
                text = text,
                fontSize = 16.tu,
                fontWeight = FontWeight.W400,
                color = black,
                modifier = Modifier.weight(1f)
            )

            iconTail?.let {
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = iconTail),
                        contentDescription = "DropDownMenuIconTail",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
        DropdownMenu(
            expanded = dropDownExpandedState.value,
            onDismissRequest = { dropDownExpandedState.value = false },
            modifier = Modifier.background(white)
        ) {
            items.forEach {
                androidx.compose.material.DropdownMenuItem(
                    onClick = {
                        setTextChanged(it)
                        dropDownExpandedState.value = false
                    }
                ) {
                    Text(text = it)
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewDropDownMenuCustom() {
    TwomIllustratedBookPreview {
        DropDownMenuCustom(
            iconHeader = R.drawable.icon_home,
            iconTail = R.drawable.icon_login,
            label = "출발 지역",
            text = "인동",
            setTextChanged = {},
            items = emptyList()
        )
    }
}