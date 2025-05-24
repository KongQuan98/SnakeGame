package com.example.snakegame.presentation.ui.screen.settingscreen

import android.widget.Toast
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
import com.example.snakegame.presentation.ui.screen.MenuOption
import com.example.snakegame.presentation.ui.screen.SnakeAnimation
import com.example.snakegame.presentation.ui.theme.LightGreen
import com.example.snakegame.presentation.utility.ClickDebouncer
import com.example.snakegame.presentation.utility.VibrationManager.vibrate
import com.example.snakegame.presentation.viewmodel.SettingsViewModel

@Composable
fun SnakeSpeedScreen(navController: NavController) {
    var selectedIndex by remember { mutableIntStateOf(2) }
    var selectedSpeed by remember { mutableLongStateOf(150L) }

    val menuOptions = listOf(
        stringResource(id = R.string.super_slow) to 200L,
        stringResource(id = R.string.slow) to 180L,
        stringResource(id = R.string.medium) to 150L,
        stringResource(id = R.string.fast) to 110L,
        stringResource(id = R.string.super_fast) to 70L,
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
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.snake_speed),
                color = Color.Black,
                fontFamily = FontFamily(
                    Font(R.font.nokia_font)
                ),
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 45.sp
            )

            Column(
                Modifier
                    .padding(40.dp)
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
                                Toast.makeText(
                                    context,
                                    "${menuOptions[selectedIndex].first} ${context.getString(R.string.is_selected)}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
                }
            }

            Text(
                text = stringResource(id = R.string.done),
                fontFamily = FontFamily(Font(R.font.nokia_font)),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = LightGreen,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable {
                        if (ClickDebouncer.canClick()) {
                            vibrate(context)
                            navController.popBackStack() // Go back to the previous screen
                        }
                    }
                    .padding(8.dp)
                    .background(Color.Black)
                    .padding(horizontal = 32.dp, vertical = 8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}