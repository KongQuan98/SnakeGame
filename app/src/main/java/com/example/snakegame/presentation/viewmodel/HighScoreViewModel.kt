package com.example.snakegame.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snakegame.presentation.ui.screen.HighScore
import com.example.snakegame.service.repo.HighScoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HighScoreViewModel @Inject constructor(
    private val repository: HighScoreRepository
) : ViewModel() {
    fun addHighScore(name: String, score: Int, date: String) {
        viewModelScope.launch {
            repository.insert(HighScore(name = name, score = score, date = date))
        }
    }

    suspend fun getHighScores(): List<HighScore> {
        return repository.getAllHighScores()
    }

    suspend fun getHighestScores(): HighScore? {
        val list = repository.getAllHighScores()
        return list.maxByOrNull { it.score }
    }

    fun clearAllRecords() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }
}
