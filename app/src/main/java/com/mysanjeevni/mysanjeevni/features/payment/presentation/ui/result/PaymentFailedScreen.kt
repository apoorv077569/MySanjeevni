package com.mysanjeevni.mysanjeevni.features.payment.presentation.ui.result

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mysanjeevni.mysanjeevni.app.LocalIsDarkTheme
import com.mysanjeevni.mysanjeevni.utils.AutoText
import com.mysanjeevni.mysanjeevni.utils.rememberTranslatedText

private val RedError       = Color(0xFFE53935)
private val RedErrorLight  = Color(0xFFEF5350)
private val TealButton     = Color(0xFF00BFA5)
private val WavePinkLight1 = Color(0xFFFFD6D6)
private val WavePinkLight2 = Color(0xFFFFBCBC)
private val WaveDarkBg1    = Color(0xFF3A1A1A)
private val WaveDarkBg2    = Color(0xFF2E1212)
private val RingLight1     = Color(0xFFFFE8E8)
private val RingLight2     = Color(0xFFFFD0D0)
private val RingLight3     = Color(0xFFFFBCBC)
private val RingDark1      = Color(0xFF3D1515)
private val RingDark2      = Color(0xFF4A1919)
private val RingDark3      = Color(0xFF5C1F1F)

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun PaymentFailedScreen(
    reason: String,
    onHome: () -> Unit
) {
//    val isDark = isSystemInDarkTheme()
    val isDark = LocalIsDarkTheme.current || isSystemInDarkTheme()
    // Resolved colors
    val bgColor         = if (isDark) Color(0xFF121212)   else Color.White
    val titleNavy       = if (isDark) Color(0xFFE8EAF6)   else Color(0xFF1A2340)
    val subtitleGray    = if (isDark) Color(0xFF9E9E9E)    else Color(0xFF9E9E9E)
    val errorCardBg     = if (isDark) Color(0xFF2C1010)    else Color(0xFFFFEBEE)
    val errorCodeColor  = if (isDark) Color(0xFFEF9A9A)    else RedError
    val errorIconStroke = if (isDark) Color(0xFFEF9A9A)    else RedError
    val decorColor      = if (isDark) Color(0xFF7B3030)    else Color(0xFFFFAAAA)

    val ring1           = if (isDark) RingDark1            else RingLight1
    val ring2           = if (isDark) RingDark2            else RingLight2
    val ring3           = if (isDark) RingDark3            else RingLight3

    val wave1Color      = if (isDark) WaveDarkBg1          else WavePinkLight1
    val wave2Color      = if (isDark) WaveDarkBg2          else WavePinkLight2

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    ) {

        // ── Bottom waves ──────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .align(Alignment.BottomCenter)
                .drawBehind {
                    val w = size.width
                    val h = size.height

                    val wave1 = Path().apply {
                        moveTo(0f, h * 0.5f)
                        cubicTo(w * 0.15f, h * 0.2f, w * 0.45f, h * 0.7f, w * 0.6f, h * 0.4f)
                        cubicTo(w * 0.75f, h * 0.1f, w * 0.9f, h * 0.45f, w, h * 0.35f)
                        lineTo(w, h); lineTo(0f, h); close()
                    }
                    drawPath(wave1, wave1Color.copy(alpha = if (isDark) 0.6f else 0.5f))

                    val wave2 = Path().apply {
                        moveTo(0f, h * 0.75f)
                        cubicTo(w * 0.2f, h * 0.45f, w * 0.4f, h * 0.85f, w * 0.6f, h * 0.6f)
                        cubicTo(w * 0.8f, h * 0.35f, w * 0.9f, h * 0.55f, w, h * 0.5f)
                        lineTo(w, h); lineTo(0f, h); close()
                    }
                    drawPath(wave2, wave2Color.copy(alpha = if (isDark) 0.55f else 0.45f))
                }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // ── Icon with glow rings ──────────────────────────────────────────
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(220.dp)
            ) {
                // Decorative ✕ (top-left)
                AutoText(
                    text = "✕",
                    color = decorColor,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = 12.dp, y = 28.dp)
                )
                // Decorative ○ (bottom-left)
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .align(Alignment.CenterStart)
                        .offset(x = 6.dp, y = 20.dp)
                        .clip(CircleShape)
                        .drawBehind {
                            drawCircle(decorColor, radius = size.minDimension / 2, style = Stroke(2.dp.toPx()))
                        }
                )
                // Decorative ○ (top-right)
                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = (-14).dp, y = 20.dp)
                        .drawBehind {
                            drawCircle(decorColor, radius = size.minDimension / 2, style = Stroke(2.dp.toPx()))
                        }
                )
                // Decorative dot grid (right)
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .offset(x = (-4).dp, y = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    repeat(3) {
                        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            repeat(3) {
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .clip(CircleShape)
                                        .background(decorColor.copy(alpha = 0.5f))
                                )
                            }
                        }
                    }
                }
                // Warning triangle (right-mid)
                AutoText(
                    text = "⚠",
                    color = decorColor,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .offset(x = (-6).dp, y = 28.dp)
                )

                // Glow rings
                Box(
                    modifier = Modifier
                        .size(210.dp)
                        .clip(CircleShape)
                        .background(ring1.copy(alpha = 0.4f))
                )
                Box(
                    modifier = Modifier
                        .size(168.dp)
                        .clip(CircleShape)
                        .background(ring2.copy(alpha = 0.45f))
                )
                Box(
                    modifier = Modifier
                        .size(128.dp)
                        .clip(CircleShape)
                        .background(ring3.copy(alpha = 0.5f))
                )

                // Red icon circle with radial gradient
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(108.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(RedErrorLight, RedError),
                                center = Offset.Unspecified,
                                radius = 160f
                            )
                        )
                ) {
                    // Bold white X
                    AutoText(
                        text = "✕",
                        color = Color.White,
                        fontSize = 44.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            val payment = rememberTranslatedText("Payment")
            val failed = rememberTranslatedText("Failed")

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = titleNavy,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 28.sp
                        )
                    ) {
                        append(payment)
                        append(" ")
                    }
                    withStyle(
                        SpanStyle(
                            color = RedError,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 28.sp
                        )
                    ) {
                        append(failed)
                    }
                },
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ── Subtitle ──────────────────────────────────────────────────────
            AutoText(
                text = "We couldn't process your payment.\nPlease try again or use another\npayment method.",
                fontSize = 14.sp,
                color = subtitleGray,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            // ── Error code card ───────────────────────────────────────────────
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = errorCardBg
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Outlined exclamation circle
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(34.dp)
                            .drawBehind {
                                drawCircle(
                                    color = errorIconStroke,
                                    radius = size.minDimension / 2 - 1.dp.toPx(),
                                    style = Stroke(width = 2.dp.toPx())
                                )
                            }
                    ) {
                        AutoText(
                            text = "!",
                            color = errorIconStroke,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        AutoText(
                            text = "Error Code",
                            fontSize = 12.sp,
                            color = subtitleGray
                        )
                        AutoText(
                            text = reason,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = errorCodeColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ── Go Home button ────────────────────────────────────────────────
            Button(
                onClick = onHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(29.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TealButton),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Home,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                AutoText(
                    text = "Go Home",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}