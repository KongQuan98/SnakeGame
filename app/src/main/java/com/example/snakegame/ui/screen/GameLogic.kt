package com.example.snakegame.ui.screen

import SoundManager
import android.content.Context
import com.example.snakegame.datamodel.GameTypeEnum
import com.example.snakegame.ui.utility.generateRandomFood
import com.example.snakegame.ui.utility.generateWalls
import com.example.snakegame.ui.utility.vibrate
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
    private val gameType: GameTypeEnum
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

            val wallCoordinates = generateWalls() // Generate walls based on the board size and mode

            mutableState.update {
                it.copy(walls = wallCoordinates)
            }

            while (isRunning.value) {
                if (!isPaused.value) {
                    mutableState.update {
                        var speed = it.speed
                        when (gameType) {
                            GameTypeEnum.SNAKE_GAME_CLASSIC,
                            GameTypeEnum.SNAKE_GAME_MAZE, GameTypeEnum.SNAKE_GAME_WALLS -> {
                                delay(150L)
                            }

                            else -> {
                                delay(speed)
                            }
                        }

                        val newPosition = it.snake.first().let { pos ->
                            mutex.withLock {
                                when (gameType) {
                                    GameTypeEnum.SNAKE_GAME_CLASSIC, GameTypeEnum.SNAKE_GAME_SPEED -> {
                                        val newX =
                                            (pos.first + move.first + BOARD_SIZE) % BOARD_SIZE
                                        val newY =
                                            (pos.second + move.second + BOARD_SIZE) % BOARD_SIZE

                                        Pair(newX, newY)
                                    }

                                    GameTypeEnum.SNAKE_GAME_WALLS, GameTypeEnum.SNAKE_GAME_MAZE -> {
                                        val newX = pos.first + move.first
                                        val newY = pos.second + move.second

                                        // Check wall collision
                                        if (it.walls.contains(Pair(newX, newY))) {
                                            vibrate(context, 200)
                                            soundManager.playSound("bonk_wall")
                                            stopGame()
                                            return@update it.copy(isGameOver = true, score = score)
                                        }

                                        // Check if new position is outside the board
                                        if (Pair(newX, newY).first < 0 || Pair(
                                                newX,
                                                newY
                                            ).first >= BOARD_SIZE || Pair(
                                                newX,
                                                newY
                                            ).second < 0 || Pair(newX, newY).second >= BOARD_SIZE
                                        ) {
                                            vibrate(context, 200)
                                            soundManager.playSound("bonk_wall")
                                            stopGame()
                                            return@update it.copy(isGameOver = true, score = score)
                                        }

                                        Pair(newX, newY)
                                    }
                                }
                            }
                        }

                        if (newPosition == it.food) {
                            soundManager.playSound("eat")
                            snakeLength++
                            score++

                            // logic to increase speed for speed mode
                            if (gameType == GameTypeEnum.SNAKE_GAME_SPEED) {
                                if (speed > 50 && score > 0 && score % 5 == 0) {
                                    speed -= 30
                                }
                            }
                        }

                        // Game over when snake hits itself
                        if (it.snake.contains(newPosition)) {
                            vibrate(context, 200)
                            soundManager.playSound("bonk_body")
                            stopGame()
                            return@update it.copy(isGameOver = true, score = score)
                        }

                        it.copy(
                            food = if (newPosition == it.food) generateRandomFood(
                                it.snake,
                                it.walls
                            ) else it.food,
                            snake = listOf(newPosition) + it.snake.take(snakeLength - 1),
                            score = score,
                            speed = speed
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

    fun changeDirection(newMove: Pair<Int, Int>) {
        scope.launch {
            mutex.withLock {
                val currentTime = System.currentTimeMillis()
                // Only allow a direction change if enough time has passed
                if (currentTime - lastChangeTime >= coolDownTime) {
                    // Restrict opposite direction movements
                    if (move.first + newMove.first != 0 && move.second + newMove.second != 0) {
                        move = newMove
                        lastChangeTime = currentTime // Update the last change time
                    }
                }
            }
        }
    }

    companion object {
        const val BOARD_SIZE = 20
    }
}