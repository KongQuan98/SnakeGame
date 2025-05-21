package com.example.snakegame.presentation.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.snakegame.MainMenuActivity
import com.example.snakegame.presentation.utility.LocaleHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val _currentLanguage = MutableStateFlow(LocaleHelper.getLanguage(application))
    val currentLanguage: StateFlow<String> = _currentLanguage

    fun setLanguage(language: String) {
        viewModelScope.launch {
            val context = LocaleHelper.setLocale(getApplication(), language)
            _currentLanguage.value = language

            // Recreate the activity and navigate to settings screen
            val intent = Intent(context, MainMenuActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }

    fun getCurrentLanguage(): String {
        return _currentLanguage.value
    }
} 