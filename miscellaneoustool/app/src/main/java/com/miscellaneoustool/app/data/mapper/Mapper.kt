package com.miscellaneoustool.app.data.mapper

import com.miscellaneoustool.app.data.datasource.cache.database.entity.Book
import com.miscellaneoustool.app.data.model.Item
import com.miscellaneoustool.app.domain.model.CollectionModel
import com.miscellaneoustool.app.domain.model.ItemModel
import com.miscellaneoustool.app.domain.model.Stat

fun fromItemsWithStatsToCollectionModel(
    items: Map<Book, List<Item>>,
    stats: Map<Book, List<com.miscellaneoustool.app.data.datasource.cache.database.entity.Stat>>
) =
    items.map { eachItem ->
        CollectionModel(
            bookId = eachItem.key.bookId,
            stat = mutableMapOf<Stat, Double>().apply {
                runCatching {
                    stats[eachItem.key]!!.forEach { stat ->
                        put(Stat.valueOf(stat.type), stat.value)
                    }
                }.getOrDefault(emptyMap<Stat, Double>())
            },
            items = eachItem.value.map { item -> fromRegisterItemToDomain(item) }
        )
    }

private fun fromRegisterItemToDomain(item: Item) = ItemModel(
    name = item.name,
    count = item.count,
    enchantNumber = item.enchant,
    price = item.price
)

fun fromItemsToItemModel(items: List<com.miscellaneoustool.app.data.datasource.cache.database.entity.Item>) =
    items.map { item ->
        ItemModel(
            name = item.itemName,
            count = 0,
            enchantNumber = 0,
            price = item.itemPrice
        )
    }