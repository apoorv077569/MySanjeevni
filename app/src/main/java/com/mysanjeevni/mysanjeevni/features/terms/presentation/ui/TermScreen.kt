package com.mysanjeevni.mysanjeevni.features.terms.ui

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.utils.AutoText
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import com.mysanjeevni.mysanjeevni.app.LocalIsDarkTheme

/**
 * NOTE: LocalIsDarkTheme is assumed to already exist elsewhere in the app
 * (it is referenced exactly as in the original snippet). It should expose
 * a CompositionLocal<Boolean> indicating an app-level forced dark mode toggle.
 */
@Composable
fun TermsScreen(navController: NavController) {

    val isDark = LocalIsDarkTheme.current || isSystemInDarkTheme()
    val colors = termsColors(isDark)
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.screenBg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(12.dp))

        // Top bar
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = colors.onScreen
            )
        }

        // Header: title + decorative icon
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                AutoText(
                    text = "Terms of Use",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.onScreen
                )
            }
            HeaderIllustration(colors)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Notice banner
        Row(verticalAlignment = Alignment.Top) {
            IconBadge(icon = Icons.Filled.Shield, colors = colors, size = 40.dp)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                AutoText(
                    text = "PLEASE READ THESE TERMS CAREFULLY.",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.accent
                )
                Spacer(modifier = Modifier.height(2.dp))
                AutoText(
                    text = "By using My Sanjeevni, you agree to these terms.",
                    fontSize = 14.sp,
                    color = colors.subText
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // About card
        TermsCard(colors = colors) {
            Row {
                IconBadge(icon = Icons.Filled.Groups, colors = colors)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    AutoText(
                        text = "About My Sanjeevni",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.onCard
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    AutoText(
                        text = "My Sanjeevni is a healthcare platform that connects users with:",
                        fontSize = 14.sp,
                        color = colors.subText
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ServiceColumn(icon = Icons.Filled.Storefront, label = "Pharmacies", colors = colors)
                VerticalDivider(colors)
                ServiceColumn(icon = Icons.Filled.Science, label = "Labs", colors = colors)
                VerticalDivider(colors)
                ServiceColumn(icon = Icons.Filled.Person, label = "Doctors", colors = colors)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Our Role card
        TermsCard(colors = colors) {
            Row {
                IconBadge(icon = Icons.Filled.Shield, colors = colors)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    AutoText(
                        text = "Our Role",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.onCard
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    AutoText(
                        text = "We act only as an intermediary and are not responsible for services provided by third parties.",
                        fontSize = 14.sp,
                        color = colors.subText
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Important Terms card
        TermsCard(colors = colors) {
            Row {
                IconBadge(icon = Icons.Outlined.Assignment, colors = colors)
                Spacer(modifier = Modifier.width(12.dp))
                AutoText(
                    text = "Important Terms",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.onCard
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            val terms = listOf(
                "Prescription required for medicines",
                "Not for emergency use",
                "Payments via secure gateways",
                "No refund after service"
            )

            terms.forEachIndexed { index, term ->
                ChecklistRow(text = term, colors = colors)
                if (index != terms.lastIndex) {
                    Spacer(modifier = Modifier.height(10.dp))
                    DividerLine(colors)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grievance Officer card
        TermsCard(colors = colors) {
            Row {
                IconBadge(icon = Icons.Filled.Email, colors = colors)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    AutoText(
                        text = "Grievance Officer",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.onCard
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    AutoText(
                        text = "Email",
                        fontSize = 13.sp,
                        color = colors.subText
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    val emailInteractionSource = remember { MutableInteractionSource() }
                    AutoText(
                        text = "admin@mysanjeevni.com",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.accent,
                        modifier = Modifier.clickable(
                            interactionSource = emailInteractionSource,
                            indication = null
                        ) {
                            uriHandler.openUri("mailto:admin@mysanjeevni.com")
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

    }
}

// ---------- Reusable pieces ----------

@Composable
private fun HeaderIllustration(colors: TermsColors) {
    Box(
        modifier = Modifier
            .size(96.dp)
            .clip(CircleShape)
            .background(colors.iconBg),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.MedicalServices,
            contentDescription = null,
            tint = colors.accent,
            modifier = Modifier.size(44.dp)
        )
    }
}

@Composable
private fun IconBadge(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    colors: TermsColors,
    size: androidx.compose.ui.unit.Dp = 44.dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(colors.iconBg),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colors.accent,
            modifier = Modifier.size(size * 0.5f)
        )
    }
}

@Composable
private fun TermsCard(
    colors: TermsColors,
    content: @Composable ColumnScopeAlias.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(colors.cardBg)
            .padding(18.dp),
        content = content
    )
}

// Alias to avoid importing ColumnScope twice with same name conflicts in some setups.
private typealias ColumnScopeAlias = androidx.compose.foundation.layout.ColumnScope

@Composable
private fun ServiceColumn(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    colors: TermsColors
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(colors.iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = colors.accent,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        AutoText(
            text = label,
            fontSize = 14.sp,
            color = colors.onCard
        )
    }
}

@Composable
private fun VerticalDivider(colors: TermsColors) {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(56.dp)
            .background(colors.divider)
    )
}

@Composable
private fun DividerLine(colors: TermsColors) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(colors.divider)
    )
}

@Composable
private fun ChecklistRow(text: String, colors: TermsColors) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(colors.iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = colors.accent,
                modifier = Modifier.size(14.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        AutoText(
            text = text,
            fontSize = 14.sp,
            color = colors.onCard
        )
    }
}

// ---------- Theme colors ----------

private data class TermsColors(
    val screenBg: Color,
    val cardBg: Color,
    val trustBg: Color,
    val iconBg: Color,
    val accent: Color,
    val onScreen: Color,
    val onCard: Color,
    val subText: Color,
    val divider: Color
)

@Composable
private fun termsColors(isDark: Boolean): TermsColors {
    val accentTeal = Color(0xFF0F9B8E)
    return if (isDark) {
        TermsColors(
            screenBg = Color(0xFF101414),
            cardBg = Color(0xFF1A2120),
            trustBg = Color(0xFF16221F),
            iconBg = Color(0xFF1F3431),
            accent = Color(0xFF35C9B8),
            onScreen = Color(0xFFF3F5F4),
            onCard = Color(0xFFE7EBEA),
            subText = Color(0xFFA9B3B1),
            divider = Color(0xFF2A3534)
        )
    } else {
        TermsColors(
            screenBg = Color(0xFFF4F7F6),
            cardBg = Color.White,
            trustBg = Color(0xFFEAF6F3),
            iconBg = Color(0xFFE1F2EF),
            accent = accentTeal,
            onScreen = Color(0xFF141B1A),
            onCard = Color(0xFF1B2423),
            subText = Color(0xFF6B7674),
            divider = Color(0xFFE6EAE9)
        )
    }
}