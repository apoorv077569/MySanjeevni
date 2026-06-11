package com.mysanjeevni.mysanjeevni.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mysanjeevni.mysanjeevni.R
import okhttp3.internal.notify

class MyFirebaseService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM_TOKEN",token)
    }

    override fun onMessageReceived(message: RemoteMessage){
        super.onMessageReceived(message)

        val session = SessionManager(applicationContext)

        // 🔴 BLOCK if OFF
        if (!session.isNotificationEnabled()) {
            Log.d("FCM", "Notification blocked by user setting")
            return
        }

        val title = message.data["title"]
        val body = message.data["body"]

        Log.d("FCM","title: $title body: $body")

        showNotification(title ?: "Title", body ?: "Message")
    }
    private fun showNotification(title:String,body:String){
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "default"

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(this,channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.app_logo)
            .build()
        manager.notify(1,notification)
    }

}