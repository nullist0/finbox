package com.nullist.finbox

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder

class MainService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = Notification.Builder(this, "NOTIFICATION_ID")
            .setContentTitle("title")
            .setContentText("test")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        startForeground(123, notification)
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null
}
