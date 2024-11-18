package com.example.snakegame.presentation.ui.screen.settingscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.snakegame.R
import com.example.snakegame.presentation.ui.screen.MenuOption
import com.example.snakegame.presentation.ui.screen.SnakeAnimation
import com.example.snakegame.presentation.ui.theme.LightGreen
import com.example.snakegame.presentation.ui.utility.vibrate
import com.example.snakegame.presentation.viewmodel.SettingsViewModel

@Composable
fun SnakeSpeedScreen(navController: NavController) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    var selectedSpeed by remember { mutableLongStateOf(0L) }

    val menuOptions = listOf(
        "Super slow" to 200L,
        "Slow" to 180L,
        "Medium (Recommended)" to 150L,
        "Fast" to 110L,
        "Super fast" to 70L,
    )
    val context = LocalContext.current

    val viewModel: SettingsViewModel = hiltViewModel()
    val snakeSpeed by viewModel.snakeSpeed.observeAsState(initial = 150L)
    selectedSpeed = snakeSpeed

    menuOptions.forEachIndexed { index, pair ->
        if (pair.second == snakeSpeed) {
            selectedIndex = index
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(LightGreen), // Dark background to mimic old phone UI
        contentAlignment = Alignment.Center
    ) {

        SnakeAnimation()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "Settings",
                color = Color.Black,
                fontFamily = FontFamily(
                    Font(R.font.nokia_font)
                ),
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
            )

            Column(
                Modifier
                    .padding(top = 10.dp, start = 40.dp, end = 40.dp, bottom = 40.dp)
                    .border(2.dp, Color.Black), // Border for the main menu
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Menu Options
                menuOptions.forEachIndexed { index, option ->
                    MenuOption(
                        text = option.first,
                        isSelected = index == selectedIndex,
                        onClick = {
                            vibrate(context)
                            selectedIndex = index
                            option.second.let { speed ->
                                viewModel.updateSnakeSpeed(speed)
                            }
                        }
                    )
                }
            }

            Text(
                text = "Done",
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