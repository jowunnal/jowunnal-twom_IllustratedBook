package com.jinproject.twomillustratedbook.Item

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import com.jinproject.twomillustratedbook.Fragment.Alarm
import com.jinproject.twomillustratedbook.MainActivity.MainActivity
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.Receiver.AlarmReceiver
import com.jinproject.twomillustratedbook.Service.ReAlarmService

fun NotificationManager.sendNotification(message:String,img:String,code:Int,applicationContext: Context,gtime:Int){
    val contentIntent= Intent(applicationContext,MainActivity::class.java)
    val alarmIntent=Intent(applicationContext, ReAlarmService::class.java).apply {
        putExtra("msg",message)
        putExtra("img",img)
        putExtra("code",code)
        putExtra("gtime",gtime)
    }
    val timerSharedPreferences=applicationContext.getSharedPreferences("TimerSharedPref",Context.MODE_PRIVATE)
    val first=timerSharedPreferences.getInt("first",300)
    val last=timerSharedPreferences.getInt("last",0)
    val alarmPendingIntent=PendingIntent.getService(applicationContext,code,alarmIntent,PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    val pendingIntent=PendingIntent.getActivity(applicationContext,code,contentIntent,PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    val res=applicationContext.resources.getIdentifier(img,"drawable",applicationContext.packageName)
    val builder= NotificationCompat.Builder(applicationContext,"TwomBossAlarm")
        .setSmallIcon(res)
        .setContentTitle("알람")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources,res))
    if(code<300){
        builder.setContentText("띵동~ <"+message+"> 젠타임"+first+"분전 입니다.")
    }else{
        builder.setContentText("띵동~ <"+message+"> 젠타임"+last+"분전 입니다.")
        builder.addAction(R.drawable.img_add_alarm,"알람생성",alarmPendingIntent)
    }
    notify(code,builder.build())

}

fun NotificationManager.cancelNotification(){
    cancelAll()
}