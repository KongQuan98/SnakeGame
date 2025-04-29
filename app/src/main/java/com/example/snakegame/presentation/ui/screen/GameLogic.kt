package com.example.snakegame.presentation.ui.screen

import SoundManager
import android.content.Context
import com.example.snakegame.presentation.datamodel.GameTypeEnum
import com.example.snakegame.presentation.datamodel.Settings
import com.example.snakegame.presentation.ui.utility.VibrationManager.vibrate
import com.example.snakegame.presentation.ui.utility.generateRandomFood
import com.example.snakegame.presentation.ui.utility.generateWallsForMaze
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

data class State(
    val food: Pair<Int, Int>,
    val snake: List<Pair<Int, Int>>,
    val walls: List<Pair<Int, Int>> = emptyList(), // Add walls to the state
    val score: Int = 0,
    val speed: Long = 150L,
    val isGameOver: Boolean = false,
    val isPause: Boolean = false
)

class GameLogic(
    private val scope: CoroutineScope,
    private val context: Context,
    private val gameType: GameTypeEnum,
    private val wallsLevel: Int = 0,
    private val settings: Settings
) {

    private val mutex = Mutex()
    private val mutableState =
        MutableStateFlow(State(food = Pair(7, 7), snake = listOf(Pair(1, 7))))

    // Track whether the game is running
    private val isRunning = MutableStateFlow(true)

    // Track if the game is paused
    private val isPaused = MutableStateFlow(false)

    // Job for the game loop
    private var gameLoopJob: Job? = null

    val state: Flow<State> = mutableState

    private val soundManager = SoundManager(context)

    private var move = Pair(1, 0)
        set(value) {
            scope.launch {
                mutex.withLock {
                    field = value
                }
            }
        }

    init {
        startGame()
    }

    private fun startGame() {
        isRunning.value = true
        gameLoopJob = scope.launch {
            var snakeLength = 6
            var score = 0
            var mazeLevel = 1
            var snake: List<Pair<Int, Int>> // Initial starting position for the snake

            if (gameType == GameTypeEnum.SNAKE_GAME_WALLS) {
                mutableState.update {
                    it.copy(walls = generateWallsForMaze(wallsLevel))
                }
            }

            while (isRunning.value) {
                if (!isPaused.value) {
                    mutableState.update {
                        var speed = it.speed
                        when (gameType) {
                            GameTypeEnum.SNAKE_GAME_CLASSIC,
                            GameTypeEnum.SNAKE_GAME_MAZE,
                            GameTypeEnum.SNAKE_GAME_WALLS -> delay(settings.snakeSpeed)

                            else -> delay(speed)
                        }

                        val newPosition = it.snake.first().let { pos ->
                            mutex.withLock {
                                val newX = (pos.first + move.first + BOARD_SIZE) % BOARD_SIZE
                                val newY = (pos.second + move.second + BOARD_SIZE) % BOARD_SIZE

                                if (gameType == GameTypeEnum.SNAKE_GAME_WALLS
                                    || gameType == GameTypeEnum.SNAKE_GAME_MAZE
                                    || gameType == GameTypeEnum.SNAKE_GAME_SPEED
                                ) {
                                    // Wall collision check
                                    if (it.walls.contains(Pair(newX, newY))) {
                                        vibrateIfEnabled(200)
                                        playSoundIfEnabled("bonk_wall")
                                        stopGame()
                                        return@update it.copy(isGameOver = true, score = score)
                                    }
                                }

                                Pair(newX, newY)
                            }
                        }

                        // Increase score when snake eats food
                        if (newPosition == it.food) {
                            score++
                            playSoundIfEnabled("eat")
                            snakeLength++

                            // Increase maze level and reset snake if score is a multiple of 5
                            if (gameType == GameTypeEnum.SNAKE_GAME_MAZE && score % 5 == 0) {
                                mazeLevel++
                                isPaused.value = true
                                pauseGame()
                                snakeLength = 6
                                snake =
                                    listOf(Pair(1, 7)) // Reset snake position for each maze level

                                // Generate new walls for the updated maze level
                                mutableState.update { state ->
                                    state.copy(
                                        walls = generateWallsForMaze(mazeLevel),
                                        snake = snake,
                                        food = generateRandomFood(snake, state.walls)
                                    )
                                }
                            }

                            // Increase speed for SPEED mode
                            if (gameType == GameTypeEnum.SNAKE_GAME_SPEED) {
                                if (speed > 50 && score % 5 == 0) {
                                    speed -= 30
                                }
                            }
                        }

                        // Game over if snake hits itself
                        if (it.snake.contains(newPosition)) {
                            vibrateIfEnabled(200)
                            playSoundIfEnabled("bonk_body")
                            stopGame()
                            return@update it.copy(isGameOver = true, score = score)
                        }

                        snake = listOf(newPosition) + it.snake.take(snakeLength - 1)

                        it.copy(
                            food = if (newPosition == it.food) generateRandomFood(
                                it.snake,
                                it.walls
                            ) else it.food,
                            snake = snake,
                            score = score,
                            speed = speed,
                            walls = if (gameType == GameTypeEnum.SNAKE_GAME_MAZE)
                                generateWallsForMaze(mazeLevel) else generateWallsForMaze(wallsLevel)
                        )
                    }
                } else {
                    delay(50)
                }
            }
        }
    }


    fun resetGame() {
        // Stop the existing game loop before resetting
        stopGame()
        // Reset snake to default position and direction
        move = Pair(1, 0)  // Reset direction to right

        isPaused.value = false

        mutableState.update {
            State(
                food = Pair(7, 7),
                snake = listOf(Pair(1, 7)),
                score = 0,
                isGameOver = false,
                isPause = false
            )
        }

        startGame()
    }

    // Stop the game loop
    private fun stopGame() {
        isRunning.value = false
        gameLoopJob?.cancel()
    }

    // Pause the game
    fun pauseGame() {
        isPaused.value = true
        mutableState.update {
            it.copy(
                isPause = true
            )
        }
    }

    // Resume the game
    fun resumeGame() {
        isPaused.value = false
        mutableState.update {
            it.copy(
                isPause = false
            )
        }
    }

    private var lastChangeTime: Long = 0
    private val coolDownTime: Long = 100 // milliseconds

    fun changeDirection(newMove: Pair<Int, Int>, isPaused: Boolean) {
        scope.launch {
            mutex.withLock {
                val currentTime = System.currentTimeMillis()
                // Only allow a direction change if enough time has passed
                if (currentTime - lastChangeTime >= coolDownTime && !isPaused) {
                    // Restrict opposite direction movements
                    if (move.first + newMove.first != 0 && move.second + newMove.second != 0) {
                        move = newMove
                        lastChangeTime = currentTime // Update the last change time
                    }
                }
            }
        }
    }

    private fun playSoundIfEnabled(musicFile: String) {
        if (settings.musicEnabled) {
            soundManager.playSound(musicFile)
        }
    }

    private fun vibrateIfEnabled(vibrationLevel: Long) {
        if (settings.vibrationEnabled) {
            vibrate(context, vibrationLevel)
        }
    }

    companion object {
        const val BOARD_SIZE = 20
    }
}