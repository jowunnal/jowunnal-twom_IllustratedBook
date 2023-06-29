package com.miscellaneoustool.app.ui.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import com.miscellaneoustool.app.R
import com.miscellaneoustool.app.domain.repository.TimerRepository
import com.miscellaneoustool.app.utils.sendNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    @Inject lateinit var timerRepository: TimerRepository
    override fun onReceive(p0: Context?, p1: Intent?) {
        val notificationManager =
            p0!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(p0, notificationManager)

        val msg = p1!!.getStringExtra("msg")!!
        val img = p1.getStringExtra("img")!!
        val code = p1.getIntExtra("code", 0)
        val gtime = p1.getIntExtra("gtime", 0)
        val intervalFirstTimeSetting = p1.getIntExtra("first",0)
        val intervalSecondTimeSetting = p1.getIntExtra("second",0)

        if (code > 300) {
            notificationManager.sendNotification(
                message = msg,
                img = img,
                code = code,
                applicationContext = p0,
                gtime = gtime,
                intervalSecondTimerSetting = intervalSecondTimeSetting
            )
            CoroutineScope(Dispatchers.IO).launch {
                timerRepository.deleteTimer(msg.split("<",">").first())
            }
        }
        else {
            notificationManager.sendNotification(
                message = msg,
                img = img,
                code = code,
                applicationContext = p0,
                gtime = gtime,
                intervalFirstTimerSetting = intervalFirstTimeSetting
            )
        }
    }

    private fun createNotificationChannel(
        context: Context,
        notificationManager: NotificationManager
    ) {
        val name = context.getString(R.string.channel_name)
        val descriptionText = context.getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("TwomBossAlarm", name, importance).apply {
            description = descriptionText
            enableVibration(true)
            setShowBadge(true)
            enableLights(true)
            lightColor = Color.BLUE
        }

        notificationManager.createNotificationChannel(channel)
    }

}