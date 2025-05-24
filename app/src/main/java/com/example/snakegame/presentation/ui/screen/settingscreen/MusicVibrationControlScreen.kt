package com.example.snakegame.presentation.ui.screen.settingscreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import com.example.snakegame.presentation.datamodel.MusicVibrationEnum
import com.example.snakegame.presentation.ui.screen.SnakeAnimation
import com.example.snakegame.presentation.ui.theme.LightGreen
import com.example.snakegame.presentation.utility.ClickDebouncer
import com.example.snakegame.presentation.utility.VibrationManager.vibrate
import com.example.snakegame.presentation.viewmodel.SettingsViewModel

@Composable
fun MusicVibrationControlScreen(navController: NavController) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    var selectedMusicEnabled by remember { mutableStateOf(true) }
    var selectedVibrationEnabled by remember { mutableStateOf(true) }

    val menuOptions = listOf(
        MusicVibrationEnum.MUSIC,
        MusicVibrationEnum.VIBRATION,
    )
    val context = LocalContext.current

    val viewModel: SettingsViewModel = hiltViewModel()
    val musicEnabled by viewModel.musicEnabled.observeAsState(initial = true)
    val vibrationEnabled by viewModel.vibrationEnabled.observeAsState(initial = true)

    selectedMusicEnabled = musicEnabled
    selectedVibrationEnabled = vibrationEnabled

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
                modifier = Modifier.padding(horizontal = 40.dp),
                text = stringResource(id = R.string.music_vibration),
                color = Color.Black,
                fontFamily = FontFamily(
                    Font(R.font.nokia_font)
                ),
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
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
                    val isSelected = index == selectedIndex
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (isSelected) Color.Black else LightGreen)
                            .padding(8.dp)
                            .clickable(onClick = {
                                if (ClickDebouncer.canClick()) {
                                    vibrate(context)
                                    selectedIndex = index
                                    when (option) {
                                        MusicVibrationEnum.MUSIC -> {
                                            selectedMusicEnabled = !selectedMusicEnabled
                                            viewModel.updateMusicEnabled(selectedMusicEnabled)
                                            val onOff =
                                                if (selectedMusicEnabled)
                                                    context.getString(R.string.on)
                                                else
                                                    context.getString(R.string.off)
                                            Toast
                                                .makeText(
                                                    context,
                                                    "${context.getString(R.string.music_is_switched)} $onOff",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        }

                                        MusicVibrationEnum.VIBRATION -> {
                                            selectedVibrationEnabled = !selectedVibrationEnabled
                                            viewModel.updateVibrationEnabled(
                                                selectedVibrationEnabled
                                            )
                                            val onOff =
                                                if (selectedVibrationEnabled)
                                                    context.getString(R.string.on)
                                                else
                                                    context.getString(R.string.off)
                                            Toast
                                                .makeText(
                                                    context,
                                                    "${context.getString(R.string.vibration_is_switched)} $onOff",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        }
                                    }
                                }
                            }),
                    ) {
                        val status = when (option) {
                            MusicVibrationEnum.MUSIC ->
                                if (selectedMusicEnabled)
                                    context.getString(R.string.on)
                                else
                                    context.getString(R.string.off)

                            MusicVibrationEnum.VIBRATION ->
                                if (selectedVibrationEnabled)
                                    context.getString(R.string.on)
                                else
                                    context.getString(R.string.off)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = when (option) {
                                    MusicVibrationEnum.MUSIC -> stringResource(id = R.string.music)
                                    MusicVibrationEnum.VIBRATION -> stringResource(id = R.string.vibration)
                                },
                                color = if (isSelected) LightGreen else Color.Black,
                                fontSize = 18.sp,
                                fontFamily = FontFamily(Font(R.font.nokia_font)),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start
                            )

                            Text(
                                text = status,
                                color = if (isSelected) LightGreen else Color.Black,
                                fontSize = 18.sp,
                                fontFamily = FontFamily(Font(R.font.nokia_font)),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End
                            )
                        }
                    }
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
                            navController.popBackStack()
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