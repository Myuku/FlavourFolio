package com.example.flavourfolio.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.flavourfolio.R
import com.example.flavourfolio.tabs.steps.StepsFragment


class TimerService : Service() {

    companion object {
        private const val TAG = "TimerService"
        const val CHANNEL_ID = "TimerServiceChannel"
        const val COUNTDOWN_BR = "com.example.flavourfolio"

        const val RUNNING_KEY = "countdownTimerRunning"
        const val FINISHED_KEY = "countdownTimerFinished"
        const val TIME_KEY = "countdown"
        const val DURATION_KEY = "timerDuration"
    }

    private lateinit var countDownTimer: CountDownTimer
    private lateinit var notificationManager: NotificationManager
    private var broadcastIntent = Intent(COUNTDOWN_BR)

    override fun onCreate() {
        super.onCreate()
        val notification = createNotification()
        startForeground(1, notification)
    }

    override fun onDestroy() {
        countDownTimer.cancel()
        broadcastIntent.putExtra(RUNNING_KEY, false)
        sendBroadcast(broadcastIntent)
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val input = intent.getLongExtra(DURATION_KEY, 0)
        countDownTimer = object : CountDownTimer(input, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.d(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000)
                broadcastIntent.putExtra(TIME_KEY, millisUntilFinished)
                broadcastIntent.putExtra(RUNNING_KEY, true)
                broadcastIntent.putExtra(FINISHED_KEY, false)
                sendBroadcast(broadcastIntent)
            }

            override fun onFinish() {
                broadcastIntent.putExtra(FINISHED_KEY, true)
                sendBroadcast(broadcastIntent)
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        countDownTimer.start()
        return START_NOT_STICKY
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, StepsFragment::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
            this,
            CHANNEL_ID
        )
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= 26) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "channel_name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
       return notificationBuilder
            .setContentTitle(getString(R.string.sbs_sv_notif_title))
            .setContentText(getString(R.string.sbs_sv_notif_context))
            .setSmallIcon(R.drawable.ic_timer)
            .setContentIntent(pendingIntent)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}



