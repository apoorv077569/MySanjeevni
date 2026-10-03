package com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.view.SurfaceView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.mysanjeevni.mysanjeevni.features.consult.data.agora.AgoraManager
import com.mysanjeevni.mysanjeevni.features.consult.presentation.viewmodel.AgoraCallViewModel

@Composable
fun AgoraCallScreen(
    channelName: String,
    participantType: String,
    onCallEnded: () -> Unit,
    viewModel: AgoraCallViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var agoraManager by remember { mutableStateOf<AgoraManager?>(null) }
    var remoteUid by remember { mutableStateOf<Int?>(null) }
    var isMicMuted by remember { mutableStateOf(false) }
    var isCameraMuted by remember { mutableStateOf(false) }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val cameraGranted = permissions[Manifest.permission.CAMERA] == true
            val microphoneGranted = permissions[Manifest.permission.RECORD_AUDIO] == true
            if (cameraGranted && microphoneGranted) {
                viewModel.generateToken(
                    channelName = channelName,
                    participantType = participantType
                )
            }
        }
    LaunchedEffect(channelName, participantType) {

        val cameraGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED

        val microphoneGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED

        if (cameraGranted && microphoneGranted) {

            viewModel.generateToken(
                channelName = channelName,
                participantType = participantType
            )

        } else {

            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
                )
            )
        }
    }
    LaunchedEffect(uiState.tokenData) {
        val tokenData = uiState.tokenData ?: return@LaunchedEffect
        val manager = AgoraManager(
            context = context,
            onRemoteUserJoined = { uid ->
                remoteUid = uid
            },
            onRemoteUserLeft = { uid ->
                if (remoteUid == uid) {
                    remoteUid = null
                }
            }
        )
        agoraManager = manager
        val initialized = manager.initialize(appId = tokenData.appId)
        if (initialized) {
            manager.joinChannel(
                token = tokenData.token,
                channelName = channelName,
                uid = tokenData.uid
            )
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            agoraManager?.release()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AndroidView(
            factory = {
                SurfaceView(context)
            },
            modifier = Modifier.fillMaxSize()
        ) { surfaceView ->
            remoteUid?.let { uid ->
                agoraManager?.setupRemoteVideo(
                    view = surfaceView,
                    uid = uid
                )
            }
        }
        AndroidView(
            factory = { SurfaceView(context) },
            modifier = Modifier
                .padding(top = 40.dp, end = 16.dp)
                .size(width = 120.dp, height = 170.dp)
                .align(Alignment.TopEnd)
        ) { surfaceView ->
            agoraManager?.setupLocalVideo(surfaceView)
        }
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center),
                color = Color.White
            )
        }
        uiState.error?.let { error ->
            Text(
                text = error,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(24.dp)
            )
        }
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            shape = RoundedCornerShape(40.dp),
            color = Color.Black.copy(alpha = 0.7f)
        ) {

            Row(
                modifier = Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                ),
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {

                        isMicMuted = !isMicMuted

                        agoraManager?.muteMicrophone(
                            isMicMuted
                        )
                    }
                ) {

                    Icon(
                        imageVector =
                            if (isMicMuted) {
                                Icons.Default.MicOff
                            } else {
                                Icons.Default.Mic
                            },
                        contentDescription =
                            "Microphone",
                        tint = Color.White
                    )
                }
                IconButton(
                    onClick = {

                        isCameraMuted =
                            !isCameraMuted

                        agoraManager?.muteCamera(
                            isCameraMuted
                        )
                    }
                ) {

                    Icon(
                        imageVector =
                            if (isCameraMuted) {
                                Icons.Default.VideocamOff
                            } else {
                                Icons.Default.Videocam
                            },
                        contentDescription =
                            "Camera",
                        tint = Color.White
                    )
                }
                IconButton(onClick = { agoraManager?.switchCamera() }) {
                    Icon(
                        imageVector = Icons.Default.Cameraswitch,
                        contentDescription = "Switch Camera",
                        tint = Color.White
                    )
                }
                IconButton(
                    onClick = {
                        agoraManager?.leaveChannel()
                        onCallEnded()
                    }
                ) {
                    Icon(
                        imageVector =
                            Icons.Default.CallEnd,
                        contentDescription =
                            "End Call",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}