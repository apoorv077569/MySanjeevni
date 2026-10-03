package com.mysanjeevni.mysanjeevni.features.privacy.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.app.LocalIsDarkTheme
import com.mysanjeevni.mysanjeevni.utils.AutoText
import com.mysanjeevni.mysanjeevni.utils.rememberTranslatedText

private val TealPrimary   = Color(0xFF26C6B4)
private val PurpleAccent  = Color(0xFF7B61FF)
private val TealMuted     = Color(0xFF1A8F81)
private val DarkBg        = Color(0xFF0D1B2A)
private val DarkCard      = Color(0xFF112233)
private val DarkCardAlt   = Color(0xFF0E2038)
private val DarkText      = Color(0xFFFFFFFF)
private val DarkSubText   = Color(0xFFB0C4D8)
private val DarkDivider   = Color(0xFF1E3A52)
private val LightBg       = Color(0xFFF0F6FB)
private val LightCard     = Color(0xFFFFFFFF)
private val LightCardAlt  = Color(0xFFE8F4FF)
private val LightText     = Color(0xFF0D1B2A)
private val LightSubText  = Color(0xFF4A6A85)
private val LightDivider  = Color(0xFFD0E4F0)

@Composable
fun PrivacyScreen(navController: NavController) {
    val isDark = LocalIsDarkTheme.current

    val bg       = if (isDark) DarkBg      else LightBg
    val cardBg   = if (isDark) DarkCard    else LightCard
    val cardAlt  = if (isDark) DarkCardAlt else LightCardAlt
    val textCol  = if (isDark) DarkText    else LightText
    val subText  = if (isDark) DarkSubText else LightSubText
    val divider  = if (isDark) DarkDivider else LightDivider

    val privacy = rememberTranslatedText("Privacy")
    val policy = rememberTranslatedText("Policy")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            IconButton(
                onClick = { navController.navigateUp() },
                modifier = Modifier.padding(start = 0.dp, bottom = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = textCol
                )
            }

            // ── Header row ──────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    color = textCol,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 26.sp
                                )
                            ) {
                                append(privacy)
                                append(" ")
                            }
                            withStyle(
                                SpanStyle(
                                    color = TealPrimary,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 26.sp
                                )
                            ) {
                                append(policy)
                            }
                        }
                    )
                    Spacer(Modifier.height(8.dp))
                    AutoText(
                        text = "We are committed to protecting your privacy and keeping your data secure.",
                        color = subText,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }

                Spacer(Modifier.width(12.dp))

                // Shield illustration placeholder (teal shield icon)
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(TealMuted.copy(alpha = 0.30f), Color.Transparent)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Privacy",
                        tint = TealPrimary,
                        modifier = Modifier.size(44.dp)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Card 1: We collect user data ────────────────────────────────
            SectionCard(bg = cardBg, isDark = isDark, divider = divider) {
                SectionHeader(
                    icon = Icons.Default.Person,
                    title = "We collect user data",
                    subtitle = "to provide healthcare services",
                    iconBg = TealPrimary.copy(alpha = 0.18f),
                    iconTint = TealPrimary,
                    textCol = textCol,
                    subText = subText
                )

                HorizontalDivider(color = divider, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 4.dp))

                DataRow(
                    icon = Icons.Default.Badge,
                    title = "Personal Info",
                    subtitle = "Name, phone, address",
                    textCol = textCol,
                    subText = subText
                )

                DataRow(
                    icon = Icons.Default.MedicalInformation,
                    title = "Health Data",
                    subtitle = "Reports, prescriptions",
                    textCol = textCol,
                    subText = subText
                )

                DataRow(
                    icon = Icons.Default.CreditCard,
                    title = "Payment Data",
                    subtitle = "Via secure gateways",
                    textCol = textCol,
                    subText = subText
                )
            }

            Spacer(Modifier.height(12.dp))

            // ── Card 2: We share data with ──────────────────────────────────
            SectionCard(bg = cardBg, isDark = isDark, divider = divider) {
                SectionHeader(
                    icon = Icons.Default.Groups,
                    title = "We share data with",
                    subtitle = "trusted healthcare partners",
                    iconBg = PurpleAccent.copy(alpha = 0.18f),
                    iconTint = PurpleAccent,
                    textCol = textCol,
                    subText = subText
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SharePartnerItem(
                        icon = Icons.Default.LocalHospital,
                        label = "Pharmacies",
                        textCol = textCol,
                        subText = subText,
                        divider = divider
                    )

                    VerticalDivider(color = divider)

                    SharePartnerItem(
                        icon = Icons.Default.Science,
                        label = "Labs",
                        textCol = textCol,
                        subText = subText,
                        divider = divider
                    )

                    VerticalDivider(color = divider)

                    SharePartnerItem(
                        icon = Icons.Default.MedicalServices,
                        label = "Doctors",
                        textCol = textCol,
                        subText = subText,
                        divider = divider
                    )
                }

                Spacer(Modifier.height(4.dp))
            }

            Spacer(Modifier.height(12.dp))

            // ── Card 3: Encryption banner ────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(cardAlt)
                    .border(
                        width = 1.dp,
                        color = TealPrimary.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(TealPrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Security",
                            tint = TealPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    val yourData = rememberTranslatedText("Your data is protected with")
                    val encryption = rememberTranslatedText("encryption and secure servers.")
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    color = textCol,
                                    fontSize = 13.sp
                                )
                            ) {
                                append(yourData)
                                append(" ")
                            }
                            withStyle(
                                SpanStyle(
                                    color = TealPrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp
                                )
                            ) {
                                append(encryption)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        lineHeight = 18.sp
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Encrypted",
                        tint = TealPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            SectionCard(bg = cardBg, isDark = isDark, divider = divider) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(PurpleAccent.copy(alpha = 0.18f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = PurpleAccent,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    AutoText(
                        text = "You can",
                        color = textCol,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    UserActionButton(
                        icon = Icons.Default.Edit,
                        label = "Edit your data",
                        accentColor = PurpleAccent,
                        modifier = Modifier.weight(1f)
                    )

                    UserActionButton(
                        icon = Icons.Default.Delete,
                        label = "Request deletion",
                        accentColor = PurpleAccent,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

// ── Reusable components ───────────────────────────────────────────────────────

@Composable
private fun SectionCard(
    bg: Color,
    isDark: Boolean,
    divider: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(bg)
            .border(
                width = 0.5.dp,
                color = divider,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        content = content
    )
}

@Composable
private fun SectionHeader(
    icon: ImageVector,
    title: String,
    subtitle: String,
    iconBg: Color,
    iconTint: Color,
    textCol: Color,
    subText: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column {
            AutoText(
                text = title,
                color = textCol,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            AutoText(
                text = subtitle,
                color = subText,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun DataRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    textCol: Color,
    subText: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(TealPrimary.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TealPrimary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            AutoText(
                text = title,
                color = textCol,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )

            AutoText(
                text = subtitle,
                color = subText,
                fontSize = 12.sp
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = subText,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun SharePartnerItem(
    icon: ImageVector,
    label: String,
    textCol: Color,
    subText: Color,
    divider: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(
            horizontal = 8.dp,
            vertical = 4.dp
        )
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(PurpleAccent.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = PurpleAccent,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(Modifier.height(6.dp))

        AutoText(
            text = label,
            color = textCol,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun VerticalDivider(color: Color) {
    Box(
        modifier = Modifier
            .width(0.5.dp)
            .height(64.dp)
            .background(color)
    )
}

@Composable
private fun UserActionButton(
    icon: ImageVector,
    label: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(accentColor.copy(alpha = 0.14f))
            .border(
                width = 1.dp,
                color = accentColor.copy(alpha = 0.30f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = accentColor,
                modifier = Modifier.size(19.dp)
            )

            Spacer(Modifier.width(6.dp))

            AutoText(
                text = label,
                color = accentColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}