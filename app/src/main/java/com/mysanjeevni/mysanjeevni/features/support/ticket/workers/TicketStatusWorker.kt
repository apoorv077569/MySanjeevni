package com.mysanjeevni.mysanjeevni.features.support.ticket.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.features.support.ticket.domain.repository.SupportRepository
import com.mysanjeevni.mysanjeevni.utils.SessionManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import androidx.core.content.edit

@HiltWorker
class TicketStatusWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val supportRepository: SupportRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        return try {

            Log.d(
                "TICKET_WORKER",
                "========== WORKER STARTED =========="
            )

            val session = SessionManager(applicationContext)

            val userId = session.getUserId()

            Log.d(
                "TICKET_WORKER",
                "UserId = $userId"
            )

            if (userId.isNullOrEmpty()) {

                Log.d(
                    "TICKET_WORKER",
                    "UserId not found"
                )

                return Result.success()
            }

            val result = supportRepository.getTickets(userId)

            result.onSuccess { tickets ->

                Log.d(
                    "TICKET_WORKER",
                    "Tickets Count = ${tickets.size}"
                )

                val prefs =
                    applicationContext.getSharedPreferences(
                        "ticket_status",
                        Context.MODE_PRIVATE
                    )

                tickets.forEach { ticket ->

                    Log.d(
                        "TICKET_WORKER",
                        "--------------------------------"
                    )

                    Log.d(
                        "TICKET_WORKER",
                        "Ticket Id = ${ticket.id}"
                    )

                    Log.d(
                        "TICKET_WORKER",
                        "Subject = ${ticket.subject}"
                    )

                    Log.d(
                        "TICKET_WORKER",
                        "Current Status = ${ticket.status}"
                    )

                    val oldStatus =
                        prefs.getString(
                            ticket.id,
                            null
                        )

                    Log.d(
                        "TICKET_WORKER",
                        "Old Status = $oldStatus"
                    )

                    if (
                        oldStatus != null &&
                        oldStatus != ticket.status
                    ) {

                        Log.d(
                            "TICKET_WORKER",
                            "STATUS CHANGED"
                        )

                        Log.d(
                            "TICKET_WORKER",
                            "$oldStatus -> ${ticket.status}"
                        )

                        showNotification(
                            title = "Ticket Updated",
                            body = "${ticket.subject} is now ${ticket.status}"
                        )
                    }

                    prefs.edit {
                        putString(
                            ticket.id,
                            ticket.status
                        )
                    }

                    Log.d(
                        "TICKET_WORKER",
                        "Status Saved"
                    )
                }
            }

            result.onFailure { e ->

                Log.e(
                    "TICKET_WORKER",
                    "API FAILED = ${e.message}",
                    e
                )
            }

            Log.d(
                "TICKET_WORKER",
                "========== WORKER SUCCESS =========="
            )

            Result.success()

        } catch (e: Exception) {

            Log.e(
                "TICKET_WORKER",
                "WORKER ERROR = ${e.message}",
                e
            )

            Result.retry()
        }
    }
    private fun showNotification(
        title: String,
        body: String
    ) {

        val manager =
            applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

        val channelId = "ticket_updates"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            manager.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    "Ticket Updates",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }

        val notification =
            NotificationCompat.Builder(
                applicationContext,
                channelId
            )
                .setSmallIcon(R.drawable.app_logo)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .build()

        manager.notify(
            System.currentTimeMillis().toInt(),
            notification
        )
    }
}