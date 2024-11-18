package com.example.snakegame.service.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore("game_settings")

object SettingsKeys {
    val SNAKE_SPEED = longPreferencesKey("snake_speed")
    val VIBRATION = booleanPreferencesKey("vibration")
    val MUSIC = booleanPreferencesKey("music")
    val LANGUAGE = stringPreferencesKey("language")
}

class SettingsDataStore(context: Context) {
    private val dataStore = context.settingsDataStore

    val snakeSpeed: Flow<Long> = dataStore.data.map { it[SettingsKeys.SNAKE_SPEED] ?: 150L }
    val vibration: Flow<Boolean> = dataStore.data.map { it[SettingsKeys.VIBRATION] ?: true }
    val music: Flow<Boolean> = dataStore.data.map { it[SettingsKeys.MUSIC] ?: true }
    val language: Flow<String> = dataStore.data.map { it[SettingsKeys.LANGUAGE] ?: "English" }

    suspend fun saveSettings(speed: Long, vibration: Boolean, music: Boolean, language: String) {
        dataStore.edit {
            it[SettingsKeys.SNAKE_SPEED] = speed
            it[SettingsKeys.VIBRATION] = vibration
            it[SettingsKeys.MUSIC] = music
            it[SettingsKeys.LANGUAGE] = language
        }
    }
}
