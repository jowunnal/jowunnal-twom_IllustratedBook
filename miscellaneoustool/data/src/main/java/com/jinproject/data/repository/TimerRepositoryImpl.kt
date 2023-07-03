package com.jinproject.data.repository

import com.jinproject.core.TimerPreferences
import com.jinproject.data.datasource.cache.CacheTimerDataSource
import com.jinproject.data.datasource.cache.database.dao.TimerDao
import com.jinproject.data.mapper.toTimerModel
import com.jinproject.domain.model.MonsterType
import com.jinproject.domain.repository.TimerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TimerRepositoryImpl @Inject constructor(
    private val timerDao: TimerDao,
    private val cacheTimerDataSource: CacheTimerDataSource
) : TimerRepository {
    override fun getTimerPreferences(): Flow<TimerPreferences> =
        cacheTimerDataSource.timerPreferences


    override suspend fun addBossToFrequentlyUsedList(bossName: String) {
        cacheTimerDataSource.addBossToFrequentlyUsedList(bossName)
    }

    override suspend fun setBossToFrequentlyUsedList(bossList: List<String>) {
        cacheTimerDataSource.setBossToFrequentlyUsedList(bossList)
    }

    override suspend fun setRecentlySelectedBossClassified(bossClassified: MonsterType) {
        cacheTimerDataSource.setRecentlySelectedBossClassified(bossClassified)
    }

    override suspend fun setRecentlySelectedBossName(bossName: String) {
        cacheTimerDataSource.setRecentlySelectedBossName(bossName)
    }

    override suspend fun setIntervalTimerSetting(first: Int, second: Int) {
        cacheTimerDataSource.setIntervalTimerSetting(first, second)
    }

    override suspend fun setTimerSetting(fontSize: Int, xPos: Int, yPos: Int) {
        cacheTimerDataSource.setTimerSetting(fontSize, xPos, yPos)
    }

    override suspend fun updateTimerInterval(firstIntervalTime: Int, secondIntervalTime: Int) {
        cacheTimerDataSource.updateTimerInterval(firstIntervalTime, secondIntervalTime)
    }

    override fun getTimer() = timerDao.getTimer().map { response ->
        response.map { timer ->
            timer.toTimerModel()
        }
    }

    override suspend fun setTimer(
        id: Int,
        day: Int,
        hour: Int,
        min: Int,
        sec: Int,
        bossName: String
    ) = timerDao.setTimer(id, day, hour, min, sec, bossName)

    override suspend fun updateTimer(id: Int, day: Int, hour: Int, min: Int, sec: Int) {
        timerDao.updateTimer(
            id = id,
            day = day,
            hour = hour,
            min = min,
            sec = sec
        )
    }

    override suspend fun deleteTimer(bossName: String) = timerDao.deleteTimer(bossName)
    override suspend fun setOta(ota: Int, bossName: String) = timerDao.setOta(ota, bossName)

}