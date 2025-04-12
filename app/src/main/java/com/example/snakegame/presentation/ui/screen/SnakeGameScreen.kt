package com.example.snakegame.presentation.ui.screen

import GameOverDialog
import SaveHighScoreDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.snakegame.R
import com.example.snakegame.presentation.datamodel.ButtonTypeEnum
import com.example.snakegame.presentation.datamodel.GameTypeEnum
import com.example.snakegame.presentation.ui.screen.controloption.ArrowButtons
import com.example.snakegame.presentation.ui.screen.controloption.Joystick
import com.example.snakegame.presentation.ui.theme.DarkGreen
import com.example.snakegame.presentation.ui.theme.LightGreen
import com.example.snakegame.presentation.ui.utility.getCurrentDate
import com.example.snakegame.presentation.viewmodel.HighScoreViewModel

@Composable
fun Snake(
    game: GameLogic,
    gameType: GameTypeEnum,
    navController: NavController? = null,
    buttonType: ButtonTypeEnum = ButtonTypeEnum.ARROW_BUTTON
) {
    val state = game.state.collectAsState(initial = null)

    // state check
    val isGameOver = remember { mutableStateOf(false) }
    val isPaused = remember { mutableStateOf(false) }

    // dialog check
    val showGameOverDialog = remember { mutableStateOf(true) }
    val showHighScoreDialog = remember { mutableStateOf(false) }

    // highscore check
    val highScores = remember { mutableIntStateOf(0) }
    val viewModel: HighScoreViewModel = hiltViewModel()

    // Observe game state to detect when the game is over
    state.value?.let { gameState ->
        isGameOver.value = gameState.isGameOver // Trigger game over dialog
        isPaused.value = gameState.isPause // Trigger game over dialog with pause
        showGameOverDialog.value = isGameOver.value || isPaused.value
    }

    // Fetch high score when game is over
    LaunchedEffect(isGameOver.value) {
        if (isGameOver.value) {
            highScores.intValue = viewModel.getHighestScores()?.score ?: 0
        }
        showHighScoreDialog.value =
            isGameOver.value && (state.value?.score ?: 0) > highScores.intValue && !isPaused.value
        showGameOverDialog.value = !showHighScoreDialog.value
    }

    Box(
        modifier = Modifier.background(DarkGreen)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            state.value?.let {
                Score(it, gameType)
                Board(it, game, gameType)

                // Show SaveHighScoreDialog only if current score is greater than high score
                if (showHighScoreDialog.value) {
                    SaveHighScoreDialog(
                        score = state.value?.score ?: 0,
                        onSubmit = { name ->
                            viewModel.addHighScore(
                                name = name,
                                score = state.value?.score ?: 0,
                                date = getCurrentDate()
                            )
                            showHighScoreDialog.value = false
                            showGameOverDialog.value = true
                        },
                        onDismiss = {
                            showHighScoreDialog.value = false
                            showGameOverDialog.value =
                                true // Allow GameOverDialog to show after SaveHighScoreDialog
                        }
                    )
                }

                if (showGameOverDialog.value) {
                    // Show GameOverDialog if showGameOverDialog is true
                    GameOverDialog(
                        score = state.value?.score ?: 0,
                        onReplay = {
                            isGameOver.value = false
                            isPaused.value = false
                            game.resetGame()
                        },
                        onBackToMainMenu = {
                            isGameOver.value = false
                            isPaused.value = false
                            navController?.navigate("main_menu") {
                                popUpTo("main_menu") { inclusive = true }
                            }
                        },
                        onSeeHighScore = {
                            navController?.navigate("high_score")
                        },
                        onDismiss = {
                            isPaused.value = false
                            isGameOver.value = false
                        },
                        isPaused = isPaused.value,
                        onContinue = {
                            isPaused.value = false
                            isGameOver.value = false
                            game.resumeGame()
                        }
                    )
                }
            }

            // game control button type
            when (buttonType) {
                ButtonTypeEnum.ARROW_BUTTON -> ArrowButtons {
                    game.changeDirection(it, isPaused.value)
                }
                ButtonTypeEnum.JOYSTICK -> Joystick {
                    game.changeDirection(it, isPaused.value)
                }
            }
        }
    }
}

@Composable
fun Score(state: State, gameType: GameTypeEnum) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(
            top = 40.dp,
            bottom = 20.dp
        )
    ) {
        Text(
            text = "Score: ${state.score}",
            fontFamily = FontFamily(
                Font(R.font.nokia_font)
            ),
            color = LightGreen
        )

        // show speed number for speed mode
        if (gameType == GameTypeEnum.SNAKE_GAME_SPEED) {
            Text(
                text = "Current speed: ${state.speed}",
                fontFamily = FontFamily(
                    Font(R.font.nokia_font)
                ),
                color = LightGreen,
                modifier = Modifier.padding(start = 40.dp)
            )
        }

    }
}

@Composable
fun Board(state: State, game: GameLogic, gameType: GameTypeEnum) {
    val isBigFood = state.score % 5 == 0 && state.score > 0

    BoxWithConstraints(
        Modifier
            .padding(16.dp)
            .background(LightGreen)
    ) {
        val tileSize = maxWidth / GameLogic.BOARD_SIZE

        // board
        Box(
            Modifier
                .size(maxWidth)
                .border(2.dp, DarkGreen)
                .clickable {
                    game.pauseGame()
                }
        )

        if (gameType == GameTypeEnum.SNAKE_GAME_WALLS || gameType == GameTypeEnum.SNAKE_GAME_MAZE || gameType == GameTypeEnum.SNAKE_GAME_SPEED) {
            // Draw walls from state
            state.walls.forEach { wall ->
                Box(
                    Modifier
                        .offset(x = tileSize * wall.first, y = tileSize * wall.second)
                        .size(tileSize)
                        .padding(2.dp)
                        .drawBehind {
                            val brickWidth = size.width / 3
                            val brickHeight = size.height / 2
                            val brickColor = Color(0xFF8B4513) // Brown color for bricks

                            for (row in 0 until 2) {
                                for (col in 0 until 3) {
                                    val offsetX =
                                        if (row % 2 == 0) col * brickWidth else col * brickWidth - (brickWidth / 2)
                                    val offsetY = row * brickHeight
                                    drawRect(
                                        color = brickColor,
                                        topLeft = Offset(offsetX, offsetY),
                                        size = Size(brickWidth, brickHeight),
                                        style = Fill
                                    )
                                }
                            }
                        }
                )
            }
        }

        // food
        Box(
            Modifier
                .offset(
                    x =
                    if (isBigFood)
                        tileSize * state.food.first
                    else
                        tileSize * state.food.first + (tileSize / 4),
                    y =
                    if (isBigFood)
                        tileSize * state.food.second
                    else
                        tileSize * state.food.second + (tileSize / 4),
                )
                .size(
                    if (isBigFood) tileSize else tileSize / 2
                )
                .background(
                    if (isBigFood) Color.DarkGray else DarkGreen, CircleShape
                )
        )

        // Snake
        state.snake.forEachIndexed { _, segment ->
            Box(
                modifier = Modifier
                    .offset(x = tileSize * segment.first, y = tileSize * segment.second)
                    .size(tileSize)
                    .background(DarkGreen)
            )
        }
    }
}