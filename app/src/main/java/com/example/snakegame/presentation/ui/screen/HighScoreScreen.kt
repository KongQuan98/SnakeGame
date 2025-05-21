package com.example.snakegame.presentation.ui.screen

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.snakegame.R
import com.example.snakegame.presentation.datamodel.GameTypeEnum
import com.example.snakegame.presentation.ui.theme.DarkGreen
import com.example.snakegame.presentation.ui.theme.LightGreen
import com.example.snakegame.presentation.ui.utility.VibrationManager.vibrate
import com.example.snakegame.presentation.viewmodel.GameLogicViewModel
import com.example.snakegame.presentation.viewmodel.HighScoreViewModel

// Data model to represent a high score
data class HighScore(
    val name: String,
    val score: Int,
    val wallsLevel: String? = null,
    val maxSpeedReached: String? = null
)

@Composable
fun HighScoreScreen(navController: NavController, gameMode: GameTypeEnum) {
    val context = LocalContext.current
    val highScores = remember { mutableStateListOf<HighScore>() }
    val wallsHighScore = remember { mutableStateListOf<HighScore>() }
    val speedHighScore = remember { mutableStateListOf<HighScore>() }
    val viewModel: HighScoreViewModel = hiltViewModel()
    val gameViewModel: GameLogicViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        highScores.clear()
        highScores.addAll(viewModel.getHighScores())

        wallsHighScore.clear()
        wallsHighScore.addAll(viewModel.getWallsHighScores())

        speedHighScore.clear()
        speedHighScore.addAll(viewModel.getSpeedHighScores())
    }

    val defaultIndex = when (gameMode) {
        GameTypeEnum.SNAKE_GAME_WALLS -> 1
        GameTypeEnum.SNAKE_GAME_SPEED -> 2
        else -> 0
    }

    val scrollState = rememberScrollState()
    var selected by remember { mutableIntStateOf(defaultIndex) }
    val options = listOf(
        stringResource(id = R.string.classic),
        stringResource(id = R.string.walls),
        stringResource(id = R.string.speed),
    )

    // Main layout with the title and the high score table
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGreen),
    ) {

        SnakeAnimation()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(40.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = stringResource(id = R.string.high_scores),
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.nokia_font)),
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SlidingButtonSelector(
                    options = options,
                    selectedIndex = selected,
                    onOptionSelected = { selected = it }
                )

                // High score table (name, score, date)
                when (options[selected]) {
                    stringResource(id = R.string.classic) -> HighScoreTable(highScores)
                    stringResource(id = R.string.speed) -> HighScoreTable(speedHighScore)
                    stringResource(id = R.string.walls) -> HighScoreTable(wallsHighScore)
                }

                SlidingButtonSelector(
                    options = options,
                    selectedIndex = selected,
                    onOptionSelected = { selected = it }
                )

                // Back button
                Text(
                    text = stringResource(id = R.string.back),
                    fontFamily = FontFamily(Font(R.font.nokia_font)),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = LightGreen,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clickable {
                            vibrate(context)
                            // Set restoring state flag to prevent sound
                            gameViewModel.setRestoringState(true)
                            // Only pause if the game wasn't already over
                            if (!gameViewModel.state.value.isGameOver) {
                                gameViewModel.pauseGame()
                            }
                            navController.popBackStack() // Go back to the previous screen
                        }
                        .padding(8.dp)
                        .background(Color.Black)
                        .padding(horizontal = 32.dp, vertical = 8.dp),
                    textAlign = TextAlign.Center
                )

                // Clear button
                Text(
                    text = stringResource(id = R.string.clear),
                    fontFamily = FontFamily(Font(R.font.nokia_font)),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = LightGreen,
                    modifier = Modifier
                        .clickable {
                            vibrate(context)
                            viewModel.clearAllRecords()
                            navController.popBackStack() // Go back to the previous screen
                        }
                        .padding(8.dp)
                        .background(Color.Black)
                        .padding(horizontal = 32.dp, vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun HighScoreTable(highScores: List<HighScore>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .border(2.dp, color = Color.Black)
    ) {
        // Table header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                stringResource(id = R.string.name),
                Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = LightGreen,
                fontFamily = FontFamily(Font(R.font.nokia_font)),
                fontWeight = FontWeight.Bold
            )
            Text(
                stringResource(id = R.string.score),
                Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = LightGreen,
                fontFamily = FontFamily(Font(R.font.nokia_font)),
                fontWeight = FontWeight.Bold
            )
            if (highScores.isNotEmpty() && highScores[0].maxSpeedReached != null) {
                Text(
                    stringResource(id = R.string.max_speed),
                    Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    color = LightGreen,
                    fontFamily = FontFamily(Font(R.font.nokia_font)),
                    fontWeight = FontWeight.Bold
                )
            }

            if (highScores.isNotEmpty() && highScores[0].wallsLevel != null) {
                Text(
                    stringResource(id = R.string.level),
                    Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = LightGreen,
                    fontFamily = FontFamily(Font(R.font.nokia_font)),
                    fontWeight = FontWeight.Bold
                )
            }

        }

        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Table rows for high scores
            items(highScores) { score ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        score.name,
                        Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.nokia_font))
                    )
                    Text(
                        score.score.toString(),
                        Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.nokia_font))
                    )

                    if (highScores[0].maxSpeedReached != null) {
                        Text(
                            score.maxSpeedReached.toString(),
                            Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.nokia_font))
                        )
                    }

                    if (highScores[0].wallsLevel != null) {
                        Text(
                            score.wallsLevel.toString(),
                            Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.nokia_font))
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun SlidingButtonSelector(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(LightGreen)
    ) {
        val totalWidth = with(LocalDensity.current) { constraints.maxWidth.toDp() }
        val optionWidth = totalWidth / options.size

        val animatedOffset by animateDpAsState(
            targetValue = optionWidth * selectedIndex,
            label = "OffsetAnimation"
        )

        // Sliding indicator
        Box(
            modifier = Modifier
                .offset(x = animatedOffset)
                .width(optionWidth)
                .fillMaxHeight()
                .background(Color.Black)
        )

        Row(modifier = Modifier.fillMaxSize()) {
            options.forEachIndexed { index, text ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable { onOptionSelected(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text,
                        textAlign = TextAlign.Center,
                        color = if (index == selectedIndex) LightGreen else DarkGreen,
                        fontFamily = FontFamily(Font(R.font.nokia_font))
                    )
                }
            }
        }
    }
}
