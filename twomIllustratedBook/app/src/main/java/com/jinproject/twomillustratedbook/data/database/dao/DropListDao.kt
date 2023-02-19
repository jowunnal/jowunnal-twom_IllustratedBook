package com.jinproject.twomillustratedbook.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jinproject.twomillustratedbook.data.database.Entity.*
import com.jinproject.twomillustratedbook.data.database.Entity.MonsDropItem
import com.jinproject.twomillustratedbook.data.database.Entity.Monster
import kotlinx.coroutines.flow.Flow

@Dao
interface DropListDao {
    @RewriteQueriesToDropUnusedColumns
    @Query("select * from Monster inner join MonsDropItem on Monster.monsName = MonsDropItem.mdMonsName join MonsLiveAtMap on Monster.monsName = MonsLiveAtMap.mlMonsName where MonsLiveAtMap.mlMapName like:map")
    fun getMonsterListFromMap(map: String): Flow<Map<Monster, List<MonsDropItem>>>

    @Query("select distinct Map.mapName, Map.mapImgName from Map join MonsLiveAtMap on Map.mapName = MonsLiveAtMap.mlMapName join Monster on MonsLiveAtMap.mlMonsName = Monster.monsName order by Monster.monsLevel ")
    fun getMaps(): Flow<List<com.jinproject.twomillustratedbook.data.database.Entity.Map>>

    @Query("select * from Monster where Monster.monsType like:monsterType")
    fun getMonsterByType(monsterType: String): Flow<List<Monster>>

    @Query("select * from Monster where Monster.monsName like :monsterName")
    fun getMonsInfo(monsterName: String): Flow<Monster>
}