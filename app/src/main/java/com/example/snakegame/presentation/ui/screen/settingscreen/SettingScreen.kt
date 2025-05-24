package com.example.snakegame.presentation.ui.screen.settingscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.example.snakegame.presentation.ui.screen.MenuOption
import com.example.snakegame.presentation.ui.screen.SnakeAnimation
import com.example.snakegame.presentation.ui.theme.LightGreen
import com.example.snakegame.presentation.utility.ClickDebouncer
import com.example.snakegame.presentation.utility.VibrationManager.vibrate
import com.example.snakegame.presentation.viewmodel.MenuStateViewModel

@Composable
fun SettingScreen(navController: NavController) {
    val viewModel: MenuStateViewModel = hiltViewModel()
    val selectedIndex by viewModel.settingsSelectedIndex.collectAsState()

    val menuOptions = listOf(
        stringResource(id = R.string.snake_speed) to "settings_snake_speed",
        stringResource(id = R.string.change_button_type) to "settings_change_button_type",
        stringResource(id = R.string.music_vibration) to "settings_music_vibration",
        stringResource(id = R.string.language) to "settings_language",
    )

    val context = LocalContext.current

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
                text = stringResource(id = R.string.settings),
                color = Color.Black,
                fontFamily = FontFamily(
                    Font(R.font.nokia_font)
                ),
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
            )

            Column(
                Modifier
                    .padding(top = 40.dp, start = 40.dp, end = 40.dp, bottom = 40.dp)
                    .border(2.dp, Color.Black), // Border for the main menu
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Menu Options
                menuOptions.forEachIndexed { index, option ->
                    MenuOption(text = option.first, isSelected = index == selectedIndex, onClick = {
                        vibrate(context)
                        viewModel.settingsSelectedIndex.value = index
                        option.second.let { route ->
                            navController.navigate(route)
                        }
                    })
                }
            }

            Text(text = stringResource(id = R.string.back),
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
                textAlign = TextAlign.Center)
        }
    }
}