package com.mysanjeevni.mysanjeevni.features.settings.presentation.state


data class SettingsState(
    val isLoading: Boolean = false,

    // toggles
    val notificationsEnabled: Boolean = true,
    val whatsappUpdatesEnabled: Boolean = false,

    // selections
    val selectedLanguage: String = "English",
    val selectedTheme: AppTheme = AppTheme.SYSTEM,

    val error: String? = null
)

enum class AppTheme(val label: String) {
    SYSTEM("System"),
    LIGHT("Light"),
    DARK("Dark")
}