package com.jinproject.twomillustratedbook.domain.model

import com.jinproject.twomillustratedbook.data.database.Entity.Monster
import com.jinproject.twomillustratedbook.ui.screen.droplist.monster.item.MonsterState

data class MonsterModel(
    val name: String,
    val level: Int,
    val genTime: Int,
    val imgName: String,
    val type: MonsterType,
    val item: List<ItemModel>?
) {
    fun toMonsterState() = MonsterState(
        name = name,
        level = level,
        genTime = genTime,
        imgName = imgName,
        type = type,
        item = item?.map { itemModel ->
            itemModel.toItemState()
        }
    )

    companion object {
        fun fromMonsterResponse(response: Monster) = MonsterModel(
            name = response.monsName,
            level = response.monsLevel,
            genTime = response.monsGtime,
            imgName = response.monsImgName,
            type = MonsterType.findByStoredName(response.monsType),
            item = emptyList()
        )
    }
}
