package com.mysanjeevni.mysanjeevni.utils

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.mysanjeevni.mysanjeevni.data.remote.client.AuthApiClient
import com.mysanjeevni.mysanjeevni.data.remote.model.notification.SaveTokenRequest
import com.mysanjeevni.mysanjeevni.data.remote.model.notification.SendNotificationRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object FcmHelper {
    fun sendTokenToServer(userId:String){
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener {token ->
                Log.d("FCM_TOKEN",token)

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = AuthApiClient.api.saveFcmToken(
                            SaveTokenRequest(userId,token)
                        )
                        if (response.isSuccessful){
                            Log.d("FCM_API","TOKEN SAVED")
                        }else{
                            Log.e("FCM_API","FAILED")
                        }
                    }catch (e: Exception){
                            Log.e("FCM_API",e.message?:"")

                    }
                }
            }
    }

    fun sendNotification(
        userId: String,
        title: String,
        body: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {

            Log.d("FCM_DEBUG", "==============================")
            Log.d("FCM_DEBUG", "Sending Notification")
            Log.d("FCM_DEBUG", "UserId : $userId")
            Log.d("FCM_DEBUG", "Title  : $title")
            Log.d("FCM_DEBUG", "Body   : $body")
            Log.d("FCM_DEBUG", "==============================")

            try {

                val request = SendNotificationRequest(
                    userId = userId,
                    title = title,
                    body = body
                )

                Log.d("FCM_DEBUG", "Request = $request")

                val response = AuthApiClient.api.sendNotification(request)

                Log.d("FCM_DEBUG", "HTTP Code    : ${response.code()}")
                Log.d("FCM_DEBUG", "Is Successful: ${response.isSuccessful}")
                Log.d("FCM_DEBUG", "Message      : ${response.message()}")

                if (response.isSuccessful) {

                    Log.d("FCM_DEBUG", "Response Body: ${response.body()}")
                    Log.d("FCM", "Notification Sent")

                } else {

                    Log.e("FCM_DEBUG", "Error Body   : ${response.errorBody()?.string()}")
                    Log.e("FCM", "Notification Failed")

                }

            } catch (e: Exception) {

                Log.e("FCM_DEBUG", "Exception: ${e.message}", e)

            }
        }
    }}