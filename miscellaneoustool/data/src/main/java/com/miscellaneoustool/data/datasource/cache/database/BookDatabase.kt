package com.miscellaneoustool.data.datasource.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.miscellaneoustool.data.datasource.cache.database.dao.CollectionDao
import com.miscellaneoustool.data.datasource.cache.database.dao.DropListDao
import com.miscellaneoustool.data.datasource.cache.database.dao.TimerDao
import com.miscellaneoustool.data.datasource.cache.database.entity.Book
import com.miscellaneoustool.data.datasource.cache.database.entity.Item
import com.miscellaneoustool.data.datasource.cache.database.entity.Maps
import com.miscellaneoustool.data.datasource.cache.database.entity.MonsDropItem
import com.miscellaneoustool.data.datasource.cache.database.entity.MonsLiveAtMap
import com.miscellaneoustool.data.datasource.cache.database.entity.Monster
import com.miscellaneoustool.data.datasource.cache.database.entity.RegisterItemToBook
import com.miscellaneoustool.data.datasource.cache.database.entity.Stat
import com.miscellaneoustool.data.datasource.cache.database.entity.Timer

@Database(entities = [Book::class, Item::class, Monster::class, Maps::class, Stat::class, MonsDropItem::class, MonsLiveAtMap::class, RegisterItemToBook::class, Timer::class],
    version = 1,  exportSchema = false)
abstract class BookDatabase : RoomDatabase() {
    abstract fun getDropListDao() : DropListDao
    abstract fun getCollectionDao() : CollectionDao
    abstract  fun getTimerDao() : TimerDao
}