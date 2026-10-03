package com.mysanjeevni.mysanjeevni.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.AirplanemodeInactive
import androidx.compose.material.icons.outlined.SignalCellularAlt
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mysanjeevni.mysanjeevni.R

/**
 * Color tokens for this screen, switched automatically based on system theme.
 * Kept local (rather than relying on MaterialTheme.colorScheme) so the teal
 * accent from the design matches exactly in both light and dark mode.
 */
private data class NoInternetPalette(
    val background: Color,
    val cardBackground: Color,
    val primary: Color,
    val primaryContainer: Color,
    val blobBackground: Color,
    val titleText: Color,
    val subtitleText: Color,
    val tipText: Color,
    val divider: Color,
    val buttonText: Color
)

@Composable
private fun noInternetPalette(): NoInternetPalette = if (isSystemInDarkTheme()) {
    NoInternetPalette(
        background = Color(0xFF10151C),
        cardBackground = Color(0xFF1A222C),
        primary = Color(0xFF2DD4BF),
        primaryContainer = Color(0xFF1E3A37),
        blobBackground = Color(0xFF182026),
        titleText = Color(0xFFF2F5F7),
        subtitleText = Color(0xFF9AA5B1),
        tipText = Color(0xFFE3E8EC),
        divider = Color(0xFF2A323D),
        buttonText = Color(0xFFFFFFFF)
    )
} else {
    NoInternetPalette(
        background = Color(0xFFF2F6F7),
        cardBackground = Color(0xFFFFFFFF),
        primary = Color(0xFF14A98C),
        primaryContainer = Color(0xFFE2F3EF),
        blobBackground = Color(0xFFE7F1F1),
        titleText = Color(0xFF1B2733),
        subtitleText = Color(0xFF8B95A1),
        tipText = Color(0xFF333F4C),
        divider = Color(0xFFEDEFF1),
        buttonText = Color(0xFFFFFFFF)
    )
}

private data class TipItem(val icon: ImageVector, val text: String)

@Composable
fun NoInternetScreen(
    onRetry: () -> Unit
) {
    val palette = noInternetPalette()

    // Lottie setup is unchanged from the original screen.
    val composition = rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.sad)
    )

    val progress = animateLottieCompositionAsState(
        composition = composition.value,
        iterations = LottieConstants.IterateForever
    )

    val tips = listOf(
        TipItem(Icons.Outlined.SignalCellularAlt, "Check your mobile data or Wi-Fi connection"),
        TipItem(Icons.Outlined.AirplanemodeInactive, "Make sure airplane mode is off"),
        TipItem(Icons.Filled.Refresh, "Try moving to a place with better signal")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(palette.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(palette.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.WifiOff,
                contentDescription = null,
                tint = palette.primary,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))


        Box(
            modifier = Modifier
                .size(260.dp)
                .background(palette.blobBackground, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(
                composition = composition.value,
                progress = { progress.value },
                modifier = Modifier.size(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        AutoText(
            text = "No Internet Connection",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = palette.titleText,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        AutoText(
            text = "Please check your internet connection and try again.",
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            color = palette.subtitleText
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onRetry,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = palette.primary,
                contentColor = palette.buttonText
            ),
            modifier = Modifier
                .height(52.dp)
                .width(180.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            AutoText(
                text = "Retry",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = palette.cardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(palette.primaryContainer, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Wifi,
                            contentDescription = null,
                            tint = palette.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    AutoText(
                        text = "Tips",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = palette.primary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                tips.forEachIndexed { index, tip ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(palette.primaryContainer, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = tip.icon,
                                contentDescription = null,
                                tint = palette.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        AutoText(
                            text = tip.text,
                            fontSize = 14.sp,
                            color = palette.tipText,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (index != tips.lastIndex) {
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = palette.divider, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}