package com.example.snakegame.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snakegame.presentation.ui.screen.HighScore
import com.example.snakegame.service.repo.HighScoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SnakeGameViewModel : ViewModel() {
    fun pauseGameForNavigation() {
        isPaused = true
    }

    fun resumeGameAfterNavigation() {
        isPaused = false
    }
}
