package com.mysanjeevni.mysanjeevni.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.app.MainActivity
import com.mysanjeevni.mysanjeevni.data.local.db.AppDatabase
import com.mysanjeevni.mysanjeevni.data.local.entity.NotificationEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFirebaseService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM_TOKEN", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val session = SessionManager(applicationContext)

        if (!session.isNotificationEnabled()) {
            Log.d("FCM", "Notification blocked by user setting")
            return
        }

        val title = message.data["title"] ?: "Title"
        val body = message.data["body"] ?: "Message"

        Log.d("FCM", "title: $title body: $body")

        CoroutineScope(Dispatchers.IO).launch {

            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "mysanjeevni_db"
            ).build()

            db.notificationDao().insert(
                NotificationEntity(
                    title = title,
                    body = body
                )
            )
        }

        showNotification(title, body)
    }

    private fun showNotification(title: String, body: String) {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "default"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("destination", "notification")
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.app_logo)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        manager.notify(1, notification)
    }

}