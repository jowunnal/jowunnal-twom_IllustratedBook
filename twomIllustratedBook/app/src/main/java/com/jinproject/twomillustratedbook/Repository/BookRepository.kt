package com.jinproject.twomillustratedbook.Repository

import androidx.lifecycle.LiveData
import com.jinproject.twomillustratedbook.Database.Dao.BookDao
import com.jinproject.twomillustratedbook.Database.Entity.Timer

class BookRepository(private val bookDao: BookDao) {
    var data:String ="버섯늪지"
    fun bookList(data:String) = bookDao.getContent(data)
    val maps:LiveData<List<String>> =bookDao.getMaps()

    fun inputdata(data:String) = bookDao.getMonster(data)
    fun getNameSp(inputData:String) = bookDao.getNamedSp(inputData)
    suspend fun setTimer(day:Int,hour:Int, min:Int,sec:Int, name:String) {bookDao.setTimer(day,hour, min,sec, name)}
    val timer:LiveData<List<Timer>> =bookDao.getTimer()
    suspend fun setOta(ota:Int,name:String) = bookDao.setOta(ota,name)
    suspend fun getMonsInfo(inputData:String)=bookDao.getMonsInfo(inputData)
   /* fun getBossSp()=bookDao.getBossSp()
    fun getBigBossSp()=bookDao.getBigBossSp()*/

}