package com.mysanjeevni.mysanjeevni.utils

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.mysanjeevni.mysanjeevni.data.remote.AuthApiClient
import com.mysanjeevni.mysanjeevni.data.remote.model.SaveTokenRequest
import com.mysanjeevni.mysanjeevni.data.remote.model.SendNotificationRequest
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

    fun sendNotification(userId:String,title:String,body:String){
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val response = AuthApiClient.api.sendNotification(
                    SendNotificationRequest(userId,title,body)
                )
                if(response.isSuccessful){
                    Log.d("FCM","Notification Sent")
                }else{
                    Log.e("FCM","Failed")
                }
            }catch (e: Exception){
                Log.e("FCM",e.message?:"")
            }
        }
    }
}