package com.nullist.finbox

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel("NOTIFICATION_ID", "notification", NotificationManager.IMPORTANCE_DEFAULT)
        manager.createNotificationChannel(channel)
    }
}
