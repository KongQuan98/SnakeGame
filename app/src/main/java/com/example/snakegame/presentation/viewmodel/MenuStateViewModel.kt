package com.example.snakegame.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MenuStateViewModel @Inject constructor() : ViewModel() {
    // Main menu selection
    val mainMenuSelectedIndex = MutableStateFlow(0)

    // Special mode menu selection
    val specialModeSelectedIndex = MutableStateFlow(0)

    // Settings menu selection
    val settingsSelectedIndex = MutableStateFlow(0)

    // Walls selection
    val wallsSelectedIndex = MutableStateFlow(0)

    // High score selection
    val highScoreSelectedIndex = MutableStateFlow(0)
} 