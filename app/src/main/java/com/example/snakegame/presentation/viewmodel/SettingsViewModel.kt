package com.example.snakegame.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.snakegame.presentation.datamodel.ButtonTypeEnum
import com.example.snakegame.service.repo.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {

    val snakeSpeed = repository.snakeSpeed.asLiveData()
    val vibrationEnabled = repository.vibrationEnabled.asLiveData()
    val musicEnabled = repository.musicEnabled.asLiveData()
    val language = repository.language.asLiveData()
    val buttonType = repository.buttonType.asLiveData()
    val joyStickSize = repository.joystickSize.asLiveData().also {
        Log.d("SettingsViewModel", "Observing joystick size changes")
    }

    fun updateSnakeSpeed(speed: Long) {
        viewModelScope.launch {
            repository.updateSnakeSpeed(speed)
        }
    }

    fun updateVibrationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateVibrationEnabled(enabled)
        }
    }

    fun updateMusicEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateMusicEnabled(enabled)
        }
    }

    fun updateLanguage(language: String) {
        viewModelScope.launch {
            repository.updateLanguage(language)
        }
    }

    fun updateButtonType(buttonType: ButtonTypeEnum) {
        viewModelScope.launch {
            repository.updateButtonType(buttonType)
        }
    }

    fun updateJoyStickSize(joyStickSize: Float) {
        Log.d("SettingsViewModel", "Updating joystick size to: $joyStickSize")
        viewModelScope.launch {
            repository.updateJoyStickSize(joyStickSize)
        }
    }
}

