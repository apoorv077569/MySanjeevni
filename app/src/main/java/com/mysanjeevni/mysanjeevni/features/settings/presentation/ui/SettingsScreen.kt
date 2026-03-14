package com.mysanjeevni.mysanjeevni.features.settings.presentation.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.features.settings.presentation.state.AppTheme
import com.mysanjeevni.mysanjeevni.features.settings.presentation.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF5F7FA)
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black
    val secondaryText = if (isDark) Color.LightGray else Color.Gray
    val primaryColor = MaterialTheme.colorScheme.primary

    // Dialog states
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = textColor,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Settings",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }

        // Section: Preferences
        Text(
            text = "Preferences",
            color = secondaryText,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(cardColor)
        ) {
            SettingsSwitchItem(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                subtitle = "Order updates, reminders, offers",
                checked = state.notificationsEnabled,
                textColor = textColor,
                secondaryText = secondaryText,
                onCheckedChange = viewModel::toggleNotifications
            )

            HorizontalDivider(color = bgColor)

            SettingsSwitchItem(
                icon = Icons.Default.SupportAgent,
                title = "WhatsApp Updates",
                subtitle = "Get updates on WhatsApp",
                checked = state.whatsappUpdatesEnabled,
                textColor = textColor,
                secondaryText = secondaryText,
                onCheckedChange = viewModel::toggleWhatsappUpdates
            )

            HorizontalDivider(color = bgColor)

            SettingsNavItem(
                icon = Icons.Default.Language,
                title = "Language",
                subtitle = state.selectedLanguage,
                textColor = textColor,
                secondaryText = secondaryText,
                onClick = { showLanguageDialog = true }
            )

            HorizontalDivider(color = bgColor)

            SettingsNavItem(
                icon = Icons.Default.DarkMode,
                title = "Theme",
                subtitle = state.selectedTheme.label,
                textColor = textColor,
                secondaryText = secondaryText,
                onClick = { showThemeDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Section: Privacy & Legal
        Text(
            text = "Privacy & Legal",
            color = secondaryText,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(cardColor)
        ) {
            SettingsNavItem(
                icon = Icons.Default.Security,
                title = "Privacy",
                subtitle = "Manage your data",
                textColor = textColor,
                secondaryText = secondaryText,
                onClick = { /* navigate to Privacy */ }
            )

            HorizontalDivider(color = bgColor)

            SettingsNavItem(
                icon = Icons.Default.Policy,
                title = "Terms & Policies",
                subtitle = "Read our policies",
                textColor = textColor,
                secondaryText = secondaryText,
                onClick = { /* navigate to Policies */ }
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }

    // Language Dialog
    if (showLanguageDialog) {
        SimpleOptionDialog(
            title = "Choose Language",
            options = listOf("English", "Hindi", "Bengali"),
            selected = state.selectedLanguage,
            onDismiss = { showLanguageDialog = false },
            onSelect = {
                viewModel.setLanguage(it)
                showLanguageDialog = false
            }
        )
    }

    // Theme Dialog
    if (showThemeDialog) {
        SimpleOptionDialog(
            title = "Choose Theme",
            options = AppTheme.entries.map { it.label },
            selected = state.selectedTheme.label,
            onDismiss = { showThemeDialog = false },
            onSelect = { label ->
                val theme = AppTheme.entries.firstOrNull { it.label == label } ?: AppTheme.SYSTEM
                viewModel.setTheme(theme)
                showThemeDialog = false
            }
        )
    }
}

@Composable
private fun SettingsSwitchItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    textColor: Color,
    secondaryText: Color,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = secondaryText, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = textColor)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, fontSize = 12.sp, color = secondaryText)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun SettingsNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    textColor: Color,
    secondaryText: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = secondaryText, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = textColor)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, fontSize = 12.sp, color = secondaryText)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = secondaryText)
    }
}

@Composable
private fun SimpleOptionDialog(
    title: String,
    options: List<String>,
    selected: String,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(option) }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = option == selected,
                            onClick = { onSelect(option) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(option)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
}