package com.example.timerapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val seconds = intent?.getIntExtra(EXTRA_SECONDS, 0) ?: 0

        if (seconds <= 0) {
            stopSelf(startId)
            return START_NOT_STICKY
        }

        createNotificationChannel()

        serviceScope.launch {
            delay(seconds * 1000L)
            showNotification()
            stopSelf(startId) // сам останавливается
        }

        return START_NOT_STICKY
    }

    private fun showNotification() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Таймер завершён!")
            .setContentText("Время вышло")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Timer notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Уведомления завершения таймера"
            }

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val EXTRA_SECONDS = "extra_seconds"
        private const val CHANNEL_ID = "timer_channel_id"
        private const val NOTIFICATION_ID = 1001
    }
}