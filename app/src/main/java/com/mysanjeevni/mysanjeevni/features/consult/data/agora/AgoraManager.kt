package com.mysanjeevni.mysanjeevni.features.consult.data.agora

import android.content.Context
import android.util.Log
import android.view.SurfaceView
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.agora.rtc2.video.VideoCanvas

class AgoraManager(
    context: Context,
    private val onRemoteUserJoined: (Int) -> Unit = {},
    private val onRemoteUserLeft: (Int) -> Unit = {}
) {

    private val appContext: Context = context.applicationContext

    private var rtcEngine: RtcEngine? = null

    private var localUid: Int = 0

    private val eventHandler = object : IRtcEngineEventHandler() {

        override fun onJoinChannelSuccess(
            channel: String?,
            uid: Int,
            elapsed: Int
        ) {
            localUid = uid

            Log.d(
                TAG,
                "Joined channel successfully: channel=$channel uid=$uid"
            )
        }

        override fun onUserJoined(
            uid: Int,
            elapsed: Int
        ) {
            Log.d(
                TAG,
                "Remote user joined: uid=$uid"
            )

            remoteUserUid = uid

            onRemoteUserJoined(uid)
        }

        override fun onUserOffline(
            uid: Int,
            reason: Int
        ) {
            Log.d(
                TAG,
                "Remote user left: uid=$uid reason=$reason"
            )

            if (remoteUserUid == uid) {
                remoteUserUid = 0
            }

            onRemoteUserLeft(uid)
        }

        override fun onError(
            err: Int
        ) {
            Log.e(
                TAG,
                "Agora error: $err"
            )
        }

        override fun onLocalVideoStateChanged(
            source: Constants.VideoSourceType,
            state: Int,
            error: Int
        ) {
            Log.d(
                TAG,
                "Local video state changed: source=$source state=$state error=$error"
            )
        }

        override fun onRemoteVideoStateChanged(
            uid: Int,
            state: Int,
            reason: Int,
            elapsed: Int
        ) {
            Log.d(
                TAG,
                "Remote video state: uid=$uid state=$state reason=$reason"
            )
        }

        override fun onAudioVolumeIndication(
            speakers: Array<out AudioVolumeInfo>?,
            totalVolume: Int
        ) {
            // Optional: can be used later to show who is speaking.
        }
    }

    private var remoteUserUid: Int = 0

    fun initialize(appId: String): Boolean {

        if (rtcEngine != null) {
            Log.d(TAG, "Agora already initialized")
            return true
        }

        return try {

            Log.d(TAG, "==============================")
            Log.d(TAG, "INITIALIZING AGORA")
            Log.d(TAG, "App ID: $appId")
            Log.d(TAG, "==============================")

            val config = RtcEngineConfig().apply {
                mContext = appContext
                mAppId = appId
                mEventHandler = eventHandler

                // Video call
                mChannelProfile =
                    Constants.CHANNEL_PROFILE_COMMUNICATION
            }

            val engine = RtcEngine.create(config)

            rtcEngine = engine

            Log.d(
                TAG,
                "RtcEngine created successfully: $engine"
            )

            val audioResult = engine.enableAudio()

            Log.d(
                TAG,
                "enableAudio result: $audioResult"
            )

            val videoResult = engine.enableVideo()

            Log.d(
                TAG,
                "enableVideo result: $videoResult"
            )

            true

        } catch (e: Exception) {

            Log.e(
                TAG,
                "Agora initialization failed",
                e
            )

            rtcEngine = null

            false
        }
    }

    fun setupLocalVideo(view: SurfaceView) {

        val engine = rtcEngine

        if (engine == null) {
            Log.e(
                TAG,
                "setupLocalVideo FAILED: RtcEngine is NULL"
            )
            return
        }

        try {

            val canvas = VideoCanvas(
                view,
                VideoCanvas.RENDER_MODE_HIDDEN,
                0
            )

            engine.setupLocalVideo(canvas)

            Log.d(
                TAG,
                "Local video canvas attached"
            )

            val result = engine.startPreview()

            Log.d(
                TAG,
                "startPreview result: $result"
            )

        } catch (e: Exception) {

            Log.e(
                TAG,
                "setupLocalVideo error",
                e
            )
        }
    }

    fun setupRemoteVideo(
        view: SurfaceView,
        uid: Int
    ) {
        val engine = rtcEngine

        if (engine == null) {
            Log.e(TAG, "setupRemoteVideo: engine is NULL")
            return
        }

        if (uid == 0) {
            Log.e(TAG, "setupRemoteVideo: invalid uid=0")
            return
        }

        try {
            Log.d(
                TAG,
                "ATTACH REMOTE VIDEO: uid=$uid view=$view"
            )

            val canvas = VideoCanvas(
                view,
                VideoCanvas.RENDER_MODE_HIDDEN,
                uid
            )

            val result = engine.setupRemoteVideo(canvas)

            Log.d(
                TAG,
                "setupRemoteVideo RESULT: uid=$uid result=$result"
            )

        } catch (e: Exception) {
            Log.e(
                TAG,
                "setupRemoteVideo ERROR",
                e
            )
        }
    }

    fun joinChannel(
        token: String?,
        channelName: String,
        uid: Int
    ) {

        val engine = rtcEngine

        if (engine == null) {
            Log.e(
                TAG,
                "joinChannel FAILED: RtcEngine is NULL"
            )
            return
        }

        localUid = uid

        val options = ChannelMediaOptions().apply {

            channelProfile =
                Constants.CHANNEL_PROFILE_COMMUNICATION

            clientRoleType =
                Constants.CLIENT_ROLE_BROADCASTER

            publishMicrophoneTrack = true
            publishCameraTrack = true

            autoSubscribeAudio = true
            autoSubscribeVideo = true
        }

        try {

            val result = engine.joinChannel(
                token,
                channelName,
                uid,
                options
            )

            Log.d(
                TAG,
                "joinChannel result=$result"
            )

        } catch (e: Exception) {

            Log.e(
                TAG,
                "joinChannel error",
                e
            )
        }
    }

    fun muteMicrophone(
        mute: Boolean
    ) {

        val engine = rtcEngine

        if (engine == null) {
            Log.e(
                TAG,
                "muteMicrophone FAILED: engine null"
            )
            return
        }

        val result =
            engine.muteLocalAudioStream(mute)

        Log.d(
            TAG,
            "Microphone muted=$mute result=$result"
        )
    }

    fun muteCamera(
        mute: Boolean
    ) {

        val engine = rtcEngine

        if (engine == null) {
            Log.e(
                TAG,
                "muteCamera FAILED: engine null"
            )
            return
        }

        val result =
            engine.muteLocalVideoStream(mute)

        Log.d(
            TAG,
            "Camera muted=$mute result=$result"
        )
    }

    fun switchCamera() {

        val engine = rtcEngine

        if (engine == null) {
            Log.e(
                TAG,
                "switchCamera FAILED: engine null"
            )
            return
        }

        val result = engine.switchCamera()

        Log.d(
            TAG,
            "Camera switched result=$result"
        )
    }

    fun leaveChannel() {

        val engine = rtcEngine ?: return

        try {

            engine.stopPreview()
            engine.leaveChannel()

            Log.d(
                TAG,
                "Left Agora channel"
            )

        } catch (e: Exception) {

            Log.e(
                TAG,
                "leaveChannel error",
                e
            )
        }
    }

    fun release() {

        try {

            rtcEngine?.stopPreview()
            rtcEngine?.leaveChannel()

            RtcEngine.destroy()

            rtcEngine = null
            remoteUserUid = 0
            localUid = 0

            Log.d(
                TAG,
                "Agora engine released"
            )

        } catch (e: Exception) {

            Log.e(
                TAG,
                "Agora release error",
                e
            )
        }
    }

    fun getRemoteUserUid(): Int {
        return remoteUserUid
    }

    fun getRtcEngine(): RtcEngine? {
        return rtcEngine
    }

    companion object {
        private const val TAG = "AgoraManager"
    }
}