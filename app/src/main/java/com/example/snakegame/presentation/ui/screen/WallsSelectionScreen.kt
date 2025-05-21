package com.example.snakegame.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.snakegame.presentation.ui.utility.VibrationManager.vibrate
import com.example.snakegame.presentation.viewmodel.MenuStateViewModel

@Composable
fun WallsSelectionScreen(navController: NavController) {
    val viewModel: MenuStateViewModel = hiltViewModel()
    val selectedIndex by viewModel.wallsSelectedIndex.collectAsState()

    val context = LocalContext.current

    Box(
        Modifier
            .fillMaxSize()
            .background(LightGreen), // Dark background to mimic old phone UI
        contentAlignment = Alignment.Center
    ) {

        SnakeAnimation()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Title
            Text(
                text = stringResource(id = R.string.walls_challenge),
                color = Color.Black,
                fontFamily = FontFamily(
                    Font(R.font.nokia_font)
                ),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = stringResource(id = R.string.choose_level),
                color = Color.Black,
                fontFamily = FontFamily(
                    Font(R.font.nokia_font)
                ),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 20.dp)
            )

            LazyColumn(
                Modifier
                    .padding(top = 10.dp, start = 40.dp, end = 40.dp, bottom = 40.dp)
                    .border(2.dp, Color.Black), // Border for the main menu
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Menu Options
                val levels = (1..9).toList()
                itemsIndexed(levels) { index, level ->
                    MenuOption(
                        text = "${stringResource(id = R.string.level)} $level",
                        isSelected = index == selectedIndex,
                        onClick = {
                            vibrate(context)
                            viewModel.wallsSelectedIndex.value = index
                            navController.navigate("snake_game_walls/$level")
                        }
                    )
                }
            }

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
        }
    }
}