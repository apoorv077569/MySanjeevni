package com.mysanjeevni.mysanjeevni.features.settings.presentation.viewmodel


import androidx.lifecycle.ViewModel
import com.mysanjeevni.mysanjeevni.features.settings.presentation.state.AppTheme
import com.mysanjeevni.mysanjeevni.features.settings.presentation.state.SettingsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    fun toggleNotifications(enabled: Boolean) {
        _state.update { it.copy(notificationsEnabled = enabled) }
    }

    fun toggleWhatsappUpdates(enabled: Boolean) {
        _state.update { it.copy(whatsappUpdatesEnabled = enabled) }
    }

    fun setLanguage(language: String) {
        _state.update { it.copy(selectedLanguage = language) }
    }

    fun setTheme(theme: AppTheme) {
        _state.update { it.copy(selectedTheme = theme) }
    }
}