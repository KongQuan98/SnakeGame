package com.example.snakegame.ui.screen

import GameOverDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.snakegame.R
import com.example.snakegame.datamodel.GameTypeEnum
import com.example.snakegame.ui.theme.DarkGreen
import com.example.snakegame.ui.theme.LightGreen
import com.example.snakegame.ui.utility.vibrate

@Composable
fun Snake(
    game: GameLogic,
    gameType: GameTypeEnum,
    navController: NavController? = null
) {
    val state = game.state.collectAsState(initial = null)
    val isGameOver = remember { mutableStateOf(false) }
    val isPaused = remember { mutableStateOf(false) }

    // Observe game state to detect when the game is over
    state.value?.let { gameState ->
        isGameOver.value = gameState.isGameOver // Trigger game over dialog
        isPaused.value = gameState.isPause // Trigger game over dialog with pause
    }

    Box(
        modifier = Modifier
            .background(DarkGreen)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            state.value?.let {
                Score(it, game, gameType)

                Board(it, game, gameType)

                if (isGameOver.value || isPaused.value) {
//                    var highScores = 0
//                    val viewModel: HighScoreViewModel = hiltViewModel()

//                    LaunchedEffect(Unit) {
//                        highScores = viewModel.getHighestScores()?.score ?: 0
//                    }

//                    if (it.score > highScores) {
//                        SaveHighScoreDialog(
//                            score = it.score,
//                            onSubmit = { name ->
//                                viewModel.addHighScore(name = name, score = it.score, date = getCurrentDate())
//                            }
//                        )
//                     }

                    GameOverDialog(
                        score = state.value?.score ?: 0,
                        onReplay = {
                            isGameOver.value = false // Hide the dialog
                            isPaused.value = false
                            game.resetGame() // Reset the game
                        },
                        onBackToMainMenu = {
                            isGameOver.value = false
                            isPaused.value = false
                            navController?.navigate("main_menu") {
                                popUpTo("main_menu") { inclusive = true }
                            }
                        },
                        onSeeHighScore = {
                            // Navigate to high score screen (add implementation later)
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

            Buttons {
                game.changeDirection(it)
            }
        }
    }

}

@Composable
fun Buttons(onDirectionChange: (Pair<Int, Int>) -> Unit) {
    val buttonSize = Modifier.size(64.dp)
    val context = LocalContext.current
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
        Button(
            onClick = {
                onDirectionChange(Pair(0, -1))
                vibrate(context)
            },
            modifier = buttonSize,
            colors = ButtonDefaults.buttonColors(backgroundColor = LightGreen)
        ) {
            Icon(Icons.Default.KeyboardArrowUp, null)
        }
        Row {
            Button(
                onClick = {
                    onDirectionChange(Pair(-1, 0))
                    vibrate(context)
                },
                modifier = buttonSize,
                colors = ButtonDefaults.buttonColors(backgroundColor = LightGreen)
            ) {
                Icon(Icons.Default.KeyboardArrowLeft, null)
            }
            Spacer(modifier = buttonSize)
            Button(
                onClick = {
                    onDirectionChange(Pair(1, 0))
                    vibrate(context)
                },
                modifier = buttonSize,
                colors = ButtonDefaults.buttonColors(backgroundColor = LightGreen)
            ) {
                Icon(Icons.Default.KeyboardArrowRight, null)
            }
        }
        Button(
            onClick = {
                onDirectionChange(Pair(0, 1))
                vibrate(context)
            },
            modifier = buttonSize,
            colors = ButtonDefaults.buttonColors(backgroundColor = LightGreen)
        ) {
            Icon(Icons.Default.KeyboardArrowDown, null)
        }
    }
}

@Composable
fun Score(state: State, game: GameLogic, gameType: GameTypeEnum) {
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

        if (gameType == GameTypeEnum.SNAKE_GAME_WALLS) {
            // Draw walls from state
            state.walls.forEach { wall ->
                Box(
                    Modifier
                        .offset(x = tileSize * wall.first, y = tileSize * wall.second)
                        .size(tileSize)
                        .padding(2.dp)
                        .background(DarkGreen, RoundedCornerShape(4.dp))
                )
            }
        }

        // food
        Box(
            Modifier
                .offset(
                    x = tileSize * state.food.first + (tileSize / 4),
                    y = tileSize * state.food.second + (tileSize / 4)
                )
                .size(tileSize / 2)
                .background(
                    DarkGreen, CircleShape
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