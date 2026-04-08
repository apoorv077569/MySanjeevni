package com.mysanjeevni.mysanjeevni.features.home.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.utils.SessionManager
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    val context = LocalContext.current
    val session = remember { SessionManager(context) }
    val composition = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.loading)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition.value,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )

    LaunchedEffect(Unit) {
        delay(2500)
        if (session.isLoggedIn()) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        } else {
            navController.navigate(Screen.OnBoarding.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF00C853)),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(R.drawable.app_logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(110.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.app_name),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White // 🔥 important for contrast
            )

            Spacer(modifier = Modifier.height(20.dp))

            LottieAnimation(
                composition = composition.value,
                progress = { progress },
                modifier = Modifier.height(50.dp)
            )
        }
    }
}