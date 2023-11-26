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
import com.example.flavourfolio.MainActivity
import com.example.flavourfolio.R
import java.util.Locale
import kotlin.Int
import kotlin.Long


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
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private var broadcastIntent = Intent(COUNTDOWN_BR)

    override fun onCreate() {
        super.onCreate()
        val notification = createNotification()
        startForeground(1337, notification)
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
                updateNotification(millisUntilFinished)
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

    private fun updateNotification(duration: Long) {
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        var numMessages = 0

        val seconds = duration / 1000 % 60
        val minutes = duration / (1000 * 60) % 60
        val hours = duration / (1000 * 60 * 60) % 60
        val time = String.format(Locale.getDefault(), "%02d : %02d : %02d", hours, minutes, seconds)

        notificationBuilder
            .setContentText(time)
            .setNumber(++numMessages)

        notificationManager.notify(
            1337,  // <-- Place your notification id here
            notificationBuilder.build()
        )
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        notificationBuilder = NotificationCompat.Builder(
            this,
            CHANNEL_ID
        )
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= 26) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "Cooking Timer",
                NotificationManager.IMPORTANCE_LOW
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



