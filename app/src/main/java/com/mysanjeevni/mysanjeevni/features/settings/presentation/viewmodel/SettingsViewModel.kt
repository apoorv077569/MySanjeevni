package com.mysanjeevni.mysanjeevni.features.settings.presentation.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.mysanjeevni.mysanjeevni.features.settings.presentation.state.AppTheme
import com.mysanjeevni.mysanjeevni.features.settings.presentation.state.SettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import androidx.core.content.edit

@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val prefs = application
        .getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)

    val indianLanguages = linkedMapOf(
        "English" to "en",
        "हिंदी (Hindi)" to "hi",
        "বাংলা (Bengali)" to "bn",
        "తెలుగు (Telugu)" to "te",
        "मराठी (Marathi)" to "mr",
        "தமிழ் (Tamil)" to "ta",
        "ગુજરાતી (Gujarati)" to "gu",
        "ಕನ್ನಡ (Kannada)" to "kn",
        "മലയാളം (Malayalam)" to "ml",
        "ਪੰਜਾਬੀ (Punjabi)" to "pa",
        "ଓଡ଼ିଆ (Odia)" to "or",
        "অসমীয়া (Assamese)" to "as",
        "اردو (Urdu)" to "ur",
        "संस्कृत (Sanskrit)" to "sa",
        "Konkani" to "kok",
        "मणिपुरी (Manipuri)" to "mni",
        "नेपाली (Nepali)" to "ne",
        "सिंधी (Sindhi)" to "sd",
        "Kashmiri" to "ks",
        "Maithili" to "mai"
    )

    private val _state = MutableStateFlow(
        SettingsState(
            notificationsEnabled = prefs.getBoolean("notifications", true),
            whatsappUpdatesEnabled = prefs.getBoolean("whatsapp_updates", false),
            selectedLanguage = prefs.getString("language", "English") ?: "English",
            selectedTheme = AppTheme.entries.firstOrNull {
                it.name == prefs.getString("theme", AppTheme.SYSTEM.name)
            } ?: AppTheme.SYSTEM
        )
    )

    val state: StateFlow<SettingsState> = _state.asStateFlow()

    fun toggleNotifications(enabled: Boolean) {
        prefs.edit { putBoolean("notifications", enabled) }
        _state.value = _state.value.copy(notificationsEnabled = enabled)
    }

    fun toggleWhatsappUpdates(enabled: Boolean) {
        prefs.edit { putBoolean("whatsapp_updates", enabled) }
        _state.value = _state.value.copy(whatsappUpdatesEnabled = enabled)
    }

    fun setLanguage(language: String) {
        prefs.edit { putString("language", language) }
        _state.value = _state.value.copy(selectedLanguage = language)
    }

    fun setTheme(theme: AppTheme) {
        prefs.edit { putString("theme", theme.name) }
        // Step 2: State update karo
        _state.value = _state.value.copy(selectedTheme = theme)
    }

    fun getLocaleCode(): String {
        return indianLanguages[_state.value.selectedLanguage] ?: "en"
    }
}