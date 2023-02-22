package com.jinproject.twomillustratedbook.ui.Service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.IBinder
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.Gravity
import android.graphics.PixelFormat
import android.view.WindowManager
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.jinproject.twomillustratedbook.data.database.Entity.Timer
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.domain.model.WeekModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class OverlayService : LifecycleService() {
    private var wm: WindowManager? = null
    private var mView: View? = null
    private var notificationManager: NotificationManager? = null
    override fun onCreate() {
        super.onCreate()

        val exitIntent = Intent(this, OverlayService::class.java).apply {
            putExtra("status", true)
        }
        val exitPendingIntent = PendingIntent.getService(
            this,
            999,
            exitIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        createNotificationChannel()

        val notification = NotificationCompat.Builder(applicationContext, "현재시간 항상 보기")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setSmallIcon(R.mipmap.ic_main)
            .setContentTitle("현재시간 항상 보기가 실행중입니다.")
            .addAction(R.drawable.img_delete_alarm, "끄기", exitPendingIntent)
            .build()
        startForeground(999, notification)

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        // inflater 를 사용하여 layout 을 가져오자
        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        // 윈도우매니저 설정

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        }
        // 위치 지정

        mView = inflater.inflate(R.layout.alarm_tv_onotherapps, null)
        wm!!.addView(mView, params)

    }

    private fun createNotificationChannel() {
        val name = "아이모 도감"
        val descriptionText = "현재시간 항상 보기"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("현재시간 항상 보기", name, importance).apply {
            description = descriptionText
            setShowBadge(true)
            enableLights(false)
            lockscreenVisibility = Notification.VISIBILITY_SECRET
            lightColor = Color.BLUE
        }
        notificationManager?.createNotificationChannel(channel)
    }

    override fun onBind(p0: Intent): IBinder? {
        super.onBind(p0)
        return null
    }

    @SuppressLint("SimpleDateFormat", "CutPasteId")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (intent?.getBooleanExtra("status", false) == true)
            stopSelf()

        lifecycleScope.launch(Dispatchers.Main) {
            while (true) {
                mView?.findViewById<TextView>(R.id.tv_currentTimes)?.text =
                    SimpleDateFormat("HH:mm:ss").format(Date(System.currentTimeMillis()))
                delay(1000)
            }
        }

        val list = intent?.getParcelableArrayListExtra<Timer>("list")
        if (list != null) {
            var strOta = ""
            for (item in list) {
                strOta += item.timerMonsName + " (" + WeekModel.findByCode(item.day) + ") " + item.hour + ":" + item.min + ":" + item.sec + "\n"
            }
            mView?.findViewById<TextView>(R.id.tv_onOtherApps)?.text = strOta
        } else {
            mView?.findViewById<TextView>(R.id.tv_onOtherApps)?.text = ""
        }

        val fontSize = intent?.getIntExtra("fontSize",14)!!
        mView?.findViewById<TextView>(R.id.tv_onOtherApps)?.textSize = fontSize.toFloat()
        mView?.findViewById<TextView>(R.id.tv_currentTimes)?.textSize = fontSize.toFloat()

        return START_STICKY
    }

    override fun onDestroy() {
        if (wm != null) {
            if (mView != null) {
                wm!!.removeView(mView); // View 초기화
                wm = null
                mView = null
            }
        }
        notificationManager = null
        super.onDestroy()
    }
}