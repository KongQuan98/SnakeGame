package com.example.snakegame.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snakegame.presentation.datamodel.GameTypeEnum
import com.example.snakegame.presentation.ui.screen.HighScore
import com.example.snakegame.service.repo.HighScoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HighScoreViewModel @Inject constructor(
    private val repository: HighScoreRepository
) : ViewModel() {
    fun addHighScore(
        name: String,
        score: Int,
        wallsLevel: String? = null,
        maxSpeedReached: String? = null,
        gameType: GameTypeEnum
    ) {
        viewModelScope.launch {
            when (gameType) {
                GameTypeEnum.SNAKE_GAME_CLASSIC -> repository.insert(
                    HighScore(
                        name = name, score = score
                    )
                )

                GameTypeEnum.SNAKE_GAME_WALLS -> repository.insertWallsHighScore(
                    HighScore(
                        name = name, score = score, wallsLevel = wallsLevel
                    )
                )

                GameTypeEnum.SNAKE_GAME_SPEED -> repository.insertSpeedHighScore(
                    HighScore(
                        name = name, score = score, maxSpeedReached = maxSpeedReached
                    )
                )

                else -> Unit
            }
        }
    }

    suspend fun getHighScores(): List<HighScore> {
        return repository.getAllHighScores()
    }

    suspend fun getWallsHighScores(): List<HighScore> {
        return repository.getWallsHighScores()
    }

    suspend fun getSpeedHighScores(): List<HighScore> {
        return repository.getSpeedHighScores()
    }

    fun clearAllRecords() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }
}
