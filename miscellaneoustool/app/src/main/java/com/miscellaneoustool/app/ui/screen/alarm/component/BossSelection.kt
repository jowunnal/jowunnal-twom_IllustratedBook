package com.miscellaneoustool.app.ui.screen.alarm.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.miscellaneoustool.domain.model.MonsterType
import com.miscellaneoustool.app.ui.screen.compose.component.DefaultButton
import com.miscellaneoustool.app.ui.screen.compose.component.DialogState
import com.miscellaneoustool.app.ui.screen.compose.component.DropDownMenuCustom
import com.miscellaneoustool.app.ui.screen.compose.component.VerticalSpacer
import com.miscellaneoustool.app.ui.screen.compose.theme.deepGray
import com.miscellaneoustool.app.ui.screen.compose.theme.lightGray
import com.miscellaneoustool.app.utils.TwomIllustratedBookPreview
import com.miscellaneoustool.app.utils.tu

@Composable
fun BossSelection(
    bossNameList: List<String>,
    recentlySelectedBossClassified: String,
    recentlySelectedBossName: String,
    frequentlyUsedBossList: List<String>,
    onClickBossItem: (String) -> Unit,
    addBossToFrequentlyUsedList: (String) -> Unit,
    removeBossFromFrequentlyUsedList: (String) -> Unit,
    setRecentlySelectedBossClassifiedChanged:(com.miscellaneoustool.domain.model.MonsterType) -> Unit,
    setRecentlySelectedBossNameChanged:(String) -> Unit,
    onOpenDialog: (DialogState) -> Unit,
    onCloseDialog: () -> Unit
) {
    Column() {
        BossSelectionHeader(
            bossNameList = bossNameList,
            recentlySelectedBossClassified = recentlySelectedBossClassified,
            recentlySelectedBossName = recentlySelectedBossName,
            addBossToFrequentlyUsedList = addBossToFrequentlyUsedList,
            setRecentlySelectedBossClassifiedChanged = setRecentlySelectedBossClassifiedChanged,
            setRecentlySelectedBossNameChanged = setRecentlySelectedBossNameChanged
        )

        VerticalSpacer(height = 16.dp)
        Divider(thickness = 1.dp, color = lightGray)
        VerticalSpacer(height = 16.dp)
        Text(
            text = "자주 사용하는 보스 목록",
            fontSize = 18.tu,
            fontWeight = FontWeight.ExtraBold,
            color = deepGray,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth()
        )
        VerticalSpacer(height = 16.dp)

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.height(150.dp)
        ) {
            itemsIndexed(frequentlyUsedBossList) { index, item ->
                BossSelectionItem(
                    bossName = item,
                    onClickBossItem = onClickBossItem,
                    removeBossFromFrequentlyUsedList = removeBossFromFrequentlyUsedList,
                    onOpenDialog = onOpenDialog,
                    onCloseDialog = onCloseDialog
                )
            }
        }
    }
}

@Composable
private fun BossSelectionHeader(
    bossNameList: List<String>,
    recentlySelectedBossClassified: String,
    recentlySelectedBossName: String,
    addBossToFrequentlyUsedList: (String) -> Unit,
    setRecentlySelectedBossClassifiedChanged:(com.miscellaneoustool.domain.model.MonsterType) -> Unit,
    setRecentlySelectedBossNameChanged:(String) -> Unit,
) {
    Column() {
        Row() {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                DropDownMenuCustom(
                    label = "보스 분류",
                    text = recentlySelectedBossClassified,
                    items = com.miscellaneoustool.domain.model.MonsterType.values().toMutableList()
                        .apply { remove(com.miscellaneoustool.domain.model.MonsterType.NORMAL) }
                        .map { monsterType -> monsterType.displayName }
                        .toList(),
                    setTextChanged = { item -> setRecentlySelectedBossClassifiedChanged(com.miscellaneoustool.domain.model.MonsterType.findByDisplayName(item)) }
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                DropDownMenuCustom(
                    label = "보스 선택",
                    text = recentlySelectedBossName,
                    items = bossNameList,
                    setTextChanged = { item -> setRecentlySelectedBossNameChanged(item) }
                )
            }
        }
        VerticalSpacer(height = 20.dp)
        DefaultButton(
            content = "추가하기",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { addBossToFrequentlyUsedList(recentlySelectedBossName) }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BossSelectionItem(
    bossName: String,
    onClickBossItem: (String) -> Unit,
    removeBossFromFrequentlyUsedList: (String) -> Unit,
    onOpenDialog: (DialogState) -> Unit,
    onCloseDialog: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DefaultButton(
            content = if (bossName.length > 4) bossName.substring(0..3) + "\n" + bossName.substring(4) else bossName,
            modifier = Modifier.combinedClickable(
                onClick = { onClickBossItem(bossName) },
                onLongClick = {
                    onOpenDialog(
                        DialogState(
                            header = "$bossName 를 삭제 하시겠습니까?",
                            positiveMessage = "예",
                            negativeMessage = "아니오",
                            onPositiveCallback = {
                                removeBossFromFrequentlyUsedList(bossName)
                                onCloseDialog()
                            },
                            onNegativeCallback = { onCloseDialog() }
                        )
                    )
                }
            ),
            fontSize = 16
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewBossSelectionItem() =
    TwomIllustratedBookPreview {
        BossSelectionItem(
            bossName = "보스이름",
            onClickBossItem = {},
            removeBossFromFrequentlyUsedList = {},
            onOpenDialog = {},
            onCloseDialog = {}
        )
    }


@Preview(showBackground = true)
@Composable
private fun PreviewBossSelection() =
    TwomIllustratedBookPreview {
        BossSelection(
            bossNameList = listOf(
                "은둔자",
                "와당카",
                "빅마마",
                "바슬라프",
                "아이요의수호병",
                "칼리고",
                "데블랑",
                "우크파나"
            ),
            recentlySelectedBossClassified = "네임드",
            recentlySelectedBossName = "보스1",
            frequentlyUsedBossList = listOf(
                "은둔자",
                "와당카",
                "빅마마",
                "바슬라프",
                "아이요의수호병",
                "칼리고",
                "데블랑",
                "우크파나"
            ),
            onClickBossItem = {},
            addBossToFrequentlyUsedList = {},
            removeBossFromFrequentlyUsedList = {},
            setRecentlySelectedBossClassifiedChanged = {},
            setRecentlySelectedBossNameChanged = {},
            onOpenDialog = {},
            onCloseDialog = {}
        )
    }
