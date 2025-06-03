package com.example.snakegame.service.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.snakegame.presentation.datamodel.ButtonTypeEnum
import com.example.snakegame.presentation.utility.VibrationManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private object PreferencesKeys {
        val SPEED = longPreferencesKey("snake_speed")
        val VIBRATION = booleanPreferencesKey("vibration_enabled")
        val MUSIC = booleanPreferencesKey("music_enabled")
        val LANGUAGE = stringPreferencesKey("language")
        val BUTTON_TYPE = stringPreferencesKey("button_type")
        val JOYSTICK_SIZE = floatPreferencesKey("joystick_size")
    }

    val snakeSpeed: Flow<Long> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.SPEED] ?: 150L // Default speed
    }

    val vibrationEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.VIBRATION] ?: true // Default enabled
    }

    val musicEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.MUSIC] ?: true // Default enabled
    }

    val language: Flow<String> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.LANGUAGE] ?: "en" // Default language
    }

    val buttonType: Flow<ButtonTypeEnum> = dataStore.data.map { preferences ->
        val buttonType =
            preferences[PreferencesKeys.BUTTON_TYPE] ?: ButtonTypeEnum.ARROW_BUTTON.name
        ButtonTypeEnum.valueOf(buttonType)
    }

    val joystickSize: Flow<Float> = dataStore.data.map { preferences ->
        val value = preferences[PreferencesKeys.JOYSTICK_SIZE] ?: 150f
        android.util.Log.d("SettingsRepository", "Reading joystick size: $value")
        value
    }

    suspend fun updateSnakeSpeed(speed: Long) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SPEED] = speed
        }
    }

    suspend fun updateVibrationEnabled(enabled: Boolean) {
        VibrationManager.isSettingVibrationEnabled = enabled
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.VIBRATION] = enabled
        }
    }

    suspend fun updateMusicEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.MUSIC] = enabled
        }
    }

    suspend fun updateLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LANGUAGE] = language
        }
    }

    suspend fun updateButtonType(buttonType: ButtonTypeEnum) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.BUTTON_TYPE] = buttonType.name
        }
    }

    suspend fun updateJoyStickSize(joyStickSize: Float) {
        android.util.Log.d("SettingsRepository", "Writing joystick size: $joyStickSize")
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.JOYSTICK_SIZE] = joyStickSize
        }
    }
}
