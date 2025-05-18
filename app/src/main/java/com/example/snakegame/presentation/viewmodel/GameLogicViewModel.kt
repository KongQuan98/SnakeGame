package com.example.snakegame.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snakegame.presentation.datamodel.GameTypeEnum
import com.example.snakegame.presentation.datamodel.Settings
import com.example.snakegame.presentation.ui.screen.GameLogic
import com.example.snakegame.presentation.ui.screen.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class GameLogicViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    // Track if the game is paused
    private val isPaused = MutableStateFlow(false)


    // Store the game state
    val state = MutableStateFlow(State(food = Pair(7, 7), snake = listOf(Pair(1, 7))))

    // Store the game type
    private val gameType = MutableStateFlow(GameTypeEnum.SNAKE_GAME_CLASSIC)

    // Store the current score
    private val currentScore = MutableStateFlow(0)

    // Store the current wall level
    val wallLevel = MutableStateFlow(0)

    // Store the snake length
    private val snakeLength = MutableStateFlow(1)

    // Store the current game instance
    private var currentGame: GameLogic? = null

    // Flag to prevent sound when restoring state
    private var isRestoringState = false

    fun getGameLogic(context: Context, settings: Settings): GameLogic {
        if (currentGame == null) {
            currentGame = GameLogic(
                scope = viewModelScope,
                context = context,
                gameType = gameType.value,
                settings = settings,
                wallsLevel = wallLevel.value
            )
        }
        return currentGame!!
    }

    // Function to pause the game
    fun pauseGame() {
        isPaused.value = true
        currentGame?.pauseGame()
    }

    // Function to resume the game
    fun resumeGame() {
        isPaused.value = false
        currentGame?.resumeGame()
    }

    // Function to update the score
    fun updateScore(score: Int) {
        if (!isRestoringState) {
            currentScore.value = score
        }
    }

    fun setWallsLevel(level: Int) {
        wallLevel.value = level
    }

    // Function to update the snake length
    fun updateSnakeLength(length: Int) {
        if (!isRestoringState) {
            snakeLength.value = length
        }
    }

    // Function to reset the game
    fun resetGame() {
        currentGame?.resetGame()
    }

    // Function to set restoring state flag
    fun setRestoringState(restoring: Boolean) {
        isRestoringState = restoring
    }

    // Function to set game type
    fun setGameType(gameTypeEnum: GameTypeEnum) {
        gameType.value = gameTypeEnum
    }

    override fun onCleared() {
        super.onCleared()
        currentGame = null
    }
}