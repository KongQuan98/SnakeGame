package com.example.snakegame.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.snakegame.presentation.ui.theme.LightGreen
import com.example.snakegame.presentation.ui.utility.vibrate
import com.example.snakegame.presentation.viewmodel.HighScoreViewModel

// Data model to represent a high score
data class HighScore(val name: String, val score: Int, val date: String)

@Composable
fun HighScoreScreen(navController: NavController) {
    val context = LocalContext.current
    val highScores = remember { mutableStateListOf<HighScore>() }
    val viewModel: HighScoreViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        highScores.clear()
        highScores.addAll(viewModel.getHighScores())
    }

    // Main layout with the title and the high score table
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGreen),
        contentAlignment = Alignment.Center
    ) {

        SnakeAnimation()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(40.dp)
        ) {
            // Title
            Text(
                text = stringResource(id = R.string.high_scores),
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.nokia_font)),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            )

            // High score table (name, score, date)
            HighScoreTable(highScores)

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
                        navController.popBackStack() // Go back to the previous screen
                    }
                    .padding(8.dp)
                    .background(Color.Black)
                    .padding(horizontal = 32.dp, vertical = 8.dp),
                textAlign = TextAlign.Center
            )

            // Back button
            Text(
                text = stringResource(id = R.string.clear),
                fontFamily = FontFamily(Font(R.font.nokia_font)),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = LightGreen,
                modifier = Modifier
                    .padding(top = 8.dp)
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

@Composable
fun HighScoreTable(highScores: List<HighScore>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
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
                "Name",
                Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = LightGreen,
                fontFamily = FontFamily(Font(R.font.nokia_font)),
                fontWeight = FontWeight.Bold
            )
            Text(
                "Score",
                Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = LightGreen,
                fontFamily = FontFamily(Font(R.font.nokia_font)),
                fontWeight = FontWeight.Bold
            )
            Text(
                "Date",
                Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = LightGreen,
                fontFamily = FontFamily(Font(R.font.nokia_font)),
                fontWeight = FontWeight.Bold
            )
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
                    Text(
                        score.date,
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