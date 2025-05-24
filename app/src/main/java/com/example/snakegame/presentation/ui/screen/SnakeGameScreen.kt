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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.snakegame.R
import com.example.snakegame.presentation.datamodel.ButtonTypeEnum
import com.example.snakegame.presentation.datamodel.GameTypeEnum
import com.example.snakegame.presentation.datamodel.Settings
import com.example.snakegame.presentation.ui.screen.controloption.ArrowButtons
import com.example.snakegame.presentation.ui.screen.controloption.Joystick
import com.example.snakegame.presentation.ui.theme.DarkGreen
import com.example.snakegame.presentation.ui.theme.LightGreen
import com.example.snakegame.presentation.utility.calculateSpeedKmPerHour
import com.example.snakegame.presentation.viewmodel.GameLogicViewModel
import com.example.snakegame.presentation.viewmodel.HighScoreViewModel
import kotlinx.coroutines.delay

@Composable
fun Snake(
    settings: Settings,
    gameType: GameTypeEnum,
    navController: NavController? = null,
    buttonType: ButtonTypeEnum = ButtonTypeEnum.ARROW_BUTTON,
    level: Int = 0
) {
    val viewModel: GameLogicViewModel = hiltViewModel()
    val context = LocalContext.current

    viewModel.setGameType(gameTypeEnum = gameType)
    viewModel.setWallsLevel(level)

    // Initialize game through ViewModel
    val gameLogic = remember {
        viewModel.getGameLogic(context, settings)
    }

    val state = gameLogic.state.collectAsState(initial = null)

    // state check
    val isGameOver = remember { mutableStateOf(false) }
    val isPaused = remember { mutableStateOf(false) }

    // dialog check
    val showGameOverDialog = remember { mutableStateOf(true) }
    val showHighScoreDialog = remember { mutableStateOf(false) }

    // highscore check
    val highScoreViewModel: HighScoreViewModel = hiltViewModel()

    // Observe game state to detect when the game is over
    state.value?.let { gameState ->
        isGameOver.value = gameState.isGameOver
        isPaused.value =
            gameState.isPause && !isGameOver.value // Only show pause dialog if not game over
        showGameOverDialog.value = isGameOver.value || isPaused.value

        // Update ViewModel state
        viewModel.updateScore(gameState.score)
        viewModel.updateSnakeLength(gameState.snake.size)

        // Reset restoring state flag after state is updated
        viewModel.setRestoringState(false)
    }

    // Fetch high score when game is over
    LaunchedEffect(isGameOver.value) {
        showHighScoreDialog.value =
            isGameOver.value && !isPaused.value && !viewModel.getHasShownSaveHighScoreDialog()
        showGameOverDialog.value = !showHighScoreDialog.value
    }

    Box(
        modifier = Modifier.background(DarkGreen)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            state.value?.let {
                Score(it, gameType)
                BonusCountdownBar(
                    foodSpawnTime = it.foodSpawnTime,
                    isBonusActive = it.isBonusActive
                )
                Board(it, gameLogic, gameType)

                // Show SaveHighScoreDialog only if current score is greater than high score
                if (showHighScoreDialog.value) {
                    SaveHighScoreDialog(
                        title = stringResource(id = R.string.too_bad),
                        subtitle = "${stringResource(id = R.string.your_score_is)} " +
                                "${state.value?.score ?: 0}, " +
                                "${stringResource(id = R.string.save_now)}?",
                        onSubmit = { name ->
                            highScoreViewModel.addHighScore(
                                name = name,
                                score = state.value?.score ?: 0,
                                wallsLevel = viewModel.wallLevel.value.toString(),
                                maxSpeedReached = calculateSpeedKmPerHour(
                                    delayMillis = state.value?.speed ?: 0
                                ).toString(),
                                gameType = gameType
                            )
                            showHighScoreDialog.value = false
                            showGameOverDialog.value = true
                            viewModel.setHasShownSaveHighScoreDialog(true)
                        },
                        onDismiss = {
                            showHighScoreDialog.value = false
                            isGameOver.value = false
                            showGameOverDialog.value = true
                            viewModel.setHasShownSaveHighScoreDialog(true)
                        }
                    )
                }

                // Show GameOverDialog if showGameOverDialog is true
                if (showGameOverDialog.value) {
                    GameOverDialog(
                        score = state.value?.score ?: 0,
                        onReplay = {
                            isGameOver.value = false
                            isPaused.value = false
                            viewModel.resetGame()
                        },
                        gameType = gameType,
                        onBackToSpecialGameMenu = {
                            isGameOver.value = false
                            isPaused.value = false
                            val navigateLocation = when (gameType) {
                                GameTypeEnum.SNAKE_GAME_WALLS -> "snake_game_walls_selection"
                                GameTypeEnum.SNAKE_GAME_SPEED -> "special_mode"
                                else -> "main_menu"
                            }
                            navController?.popBackStack(navigateLocation, false)
                        },
                        onBackToMainMenu = {
                            isGameOver.value = false
                            isPaused.value = false
                            navController?.popBackStack("main_menu", false)
                        },
                        onSeeHighScore = {
                            // Don't pause the game if it's already game over
                            if (!isGameOver.value) {
                                viewModel.pauseGame()
                            }
                            navController?.navigate("high_score/${gameType.name}")
                        },
                        onDismiss = {
                            isPaused.value = false
                            isGameOver.value = false
                        },
                        isPaused = isPaused.value,
                        onContinue = {
                            isPaused.value = false
                            viewModel.resumeGame()
                        }
                    )
                }
            }

            // game control button type
            when (buttonType) {
                ButtonTypeEnum.ARROW_BUTTON -> ArrowButtons {
                    gameLogic.changeDirection(it, isPaused.value)
                }

                ButtonTypeEnum.JOYSTICK -> Joystick {
                    gameLogic.changeDirection(it, isPaused.value)
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
            bottom = 20.dp,
            start = 16.dp,
            end = 16.dp
        )
    ) {
        Text(
            text = "${stringResource(id = R.string.score)}: ${state.score}",
            fontFamily = FontFamily(
                Font(R.font.nokia_font)
            ),
            color = LightGreen
        )

        // show speed number for speed mode
        if (gameType == GameTypeEnum.SNAKE_GAME_SPEED) {
            Text(
                maxLines = 1,
                text = "${stringResource(id = R.string.speed)}: ${
                    calculateSpeedKmPerHour(
                        delayMillis = state.speed
                    )
                } km/hr",
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
    val isBigFood = state.isBonusActive

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

@Composable
fun BonusCountdownBar(foodSpawnTime: Long, isBonusActive: Boolean) {
    if (isBonusActive) {
        val progress by produceState(initialValue = 1f) {
            while (true) {
                val elapsed = System.currentTimeMillis() - foodSpawnTime
                val remaining = (GameLogic.BONUS_TIME_LIMIT - elapsed).coerceAtLeast(0L)
                value = remaining / GameLogic.BONUS_TIME_LIMIT.toFloat()
                delay(100)
            }
        }

        if (progress > 0f) {
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color.Red,
                backgroundColor = LightGreen
            )
        }
    } else {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = DarkGreen
        )
    }
}
