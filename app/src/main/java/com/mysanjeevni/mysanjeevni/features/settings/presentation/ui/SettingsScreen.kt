package com.mysanjeevni.mysanjeevni.features.settings.presentation.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SettingsSuggest
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.settings.presentation.state.AppTheme
import com.mysanjeevni.mysanjeevni.features.settings.presentation.viewmodel.SettingsViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    // Theme based colors - state se lo
    val isDark = when (state.selectedTheme) {
        AppTheme.DARK -> true
        AppTheme.LIGHT -> false
        AppTheme.SYSTEM -> isSystemInDarkTheme()
    }
    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF5F7FA)
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black
    val secondaryText = if (isDark) Color.LightGray else Color.Gray

    var showLanguageDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
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
            AutoText(
                text = "Settings",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }

        SectionHeader("Preferences", secondaryText)

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
                onCheckedChange = { isEnabled ->
                    viewModel.toggleNotifications(isEnabled)
                    if (!isEnabled) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            context.startActivity(
                                Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                                }
                            )
                        }
                    }
                }
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
        SectionHeader("Privacy & Legal", secondaryText)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(cardColor)
        ) {
            SettingsNavItem(
                icon = Icons.Default.Security,
                title = "Privacy Policy",
                subtitle = "Read our privacy policy",
                textColor = textColor,
                secondaryText = secondaryText,
                onClick = { navController.navigate(Screen.PrivacyScreen.route) }
            )
            HorizontalDivider(color = bgColor)
            SettingsNavItem(
                icon = Icons.Default.Policy,
                title = "Terms & Conditions",
                subtitle = "Read our T&C",
                textColor = textColor,
                secondaryText = secondaryText,
                onClick = { navController.navigate(Screen.TermsScreen.route) }
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
    }

    if (showLanguageDialog) {
        LanguagePickerDialog(
            languages = viewModel.indianLanguages.keys.toList(),
            selected = state.selectedLanguage,
            onDismiss = { showLanguageDialog = false },
            onSelect = { language ->
                viewModel.setLanguage(language)
                showLanguageDialog = false
                (context as? Activity)?.recreate()
            }
        )
    }

    if (showThemeDialog) {
        ThemePickerDialog(
            selected = state.selectedTheme,
            onDismiss = { showThemeDialog = false },
            onSelect = { theme ->
                viewModel.setTheme(theme)
                showThemeDialog = false
                (context as? Activity)?.recreate()
            }
        )
    }
}

@Composable
private fun SectionHeader(title: String, color: Color) {
    AutoText(
        text = title,
        color = color,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun LanguagePickerDialog(
    languages: List<String>,
    selected: String,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                AutoText(
                    text = "Choose Language",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                HorizontalDivider()
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp)
                ) {
                    items(languages) { language ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelect(language) }
                                .padding(vertical = 8.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = language == selected,
                                onClick = { onSelect(language) }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            AutoText(
                                text = language,
                                fontSize = 15.sp,
                                fontWeight = if (language == selected)
                                    FontWeight.SemiBold else FontWeight.Normal,
                                color = if (language == selected)
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        if (language != languages.last()) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                thickness = 0.5.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                        }
                    }
                }
                HorizontalDivider()
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    AutoText("Close", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ThemePickerDialog(
    selected: AppTheme,
    onDismiss: () -> Unit,
    onSelect: (AppTheme) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { AutoText("Choose Theme", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
        text = {
            Column {
                AppTheme.entries.forEach { theme ->
                    val icon = when (theme) {
                        AppTheme.LIGHT -> Icons.Default.LightMode
                        AppTheme.DARK -> Icons.Default.DarkMode
                        AppTheme.SYSTEM -> Icons.Default.SettingsSuggest
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onSelect(theme) }
                            .background(
                                if (theme == selected)
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                else Color.Transparent
                            )
                            .padding(vertical = 10.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (theme == selected)
                                MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        AutoText(
                            text = theme.label,
                            fontSize = 15.sp,
                            modifier = Modifier.weight(1f),
                            fontWeight = if (theme == selected)
                                FontWeight.SemiBold else FontWeight.Normal,
                            color = if (theme == selected)
                                MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface
                        )
                        RadioButton(
                            selected = theme == selected,
                            onClick = { onSelect(theme) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                AutoText("Close", fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
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
            AutoText(title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = textColor)
            Spacer(modifier = Modifier.height(2.dp))
            AutoText(subtitle, fontSize = 12.sp, color = secondaryText)
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
            AutoText(title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = textColor)
            Spacer(modifier = Modifier.height(2.dp))
            AutoText(subtitle, fontSize = 12.sp, color = secondaryText)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = secondaryText)
    }
}