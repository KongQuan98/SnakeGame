package com.example.snakegame.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.snakegame.presentation.datamodel.ButtonTypeEnum
import com.example.snakegame.presentation.datamodel.GameTypeEnum
import com.example.snakegame.presentation.datamodel.Settings
import com.example.snakegame.presentation.ui.screen.HighScoreScreen
import com.example.snakegame.presentation.ui.screen.MainMenu
import com.example.snakegame.presentation.ui.screen.Snake
import com.example.snakegame.presentation.ui.screen.SpecialModeScreen
import com.example.snakegame.presentation.ui.screen.WallsSelectionScreen
import com.example.snakegame.presentation.ui.screen.settingscreen.ButtonTypeScreen
import com.example.snakegame.presentation.ui.screen.settingscreen.LanguageScreen
import com.example.snakegame.presentation.ui.screen.settingscreen.MusicVibrationControlScreen
import com.example.snakegame.presentation.ui.screen.settingscreen.SettingScreen
import com.example.snakegame.presentation.ui.screen.settingscreen.SnakeSpeedScreen
import com.example.snakegame.presentation.ui.utility.VibrationManager
import com.example.snakegame.presentation.viewmodel.SettingsViewModel
import kotlin.system.exitProcess

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val snakeSpeed by settingsViewModel.snakeSpeed.observeAsState(initial = 150L)
    val vibrationEnabled by settingsViewModel.vibrationEnabled.observeAsState(initial = true)
    val musicEnabled by settingsViewModel.musicEnabled.observeAsState(initial = true)
    val buttonType by settingsViewModel.buttonType.observeAsState(initial = ButtonTypeEnum.ARROW_BUTTON)
    val language by settingsViewModel.language.observeAsState(initial = "en")

    val settings = Settings(
        snakeSpeed,
        vibrationEnabled,
        musicEnabled,
    )

    VibrationManager.isSettingVibrationEnabled = vibrationEnabled

    NavHost(navController = navController, startDestination = "main_menu") {
        // Main Menu Screen
        composable("main_menu") {
            MainMenu(navController)
        }

        // Snake Game Screen
        composable("snake_game") {
            Snake(
                settings = settings,
                gameType = GameTypeEnum.SNAKE_GAME_CLASSIC,
                navController = navController,
                buttonType = buttonType
            )
        }

        // Add other screens like highscore, special mode, settings as needed
        composable("special_mode") {
            SpecialModeScreen(navController = navController)
        }

        composable(
            "high_score/{gamemode}",
            listOf(navArgument("gamemode") { NavType.StringType })
        ) {
            val gameMode = it.arguments?.getString("gamemode")
            HighScoreScreen(
                navController = navController,
                gameMode = gameMode?.let { enum -> GameTypeEnum.valueOf(enum) }
                    ?: GameTypeEnum.SNAKE_GAME_CLASSIC
            )
        }

        composable("settings") {
            SettingScreen(navController = navController)
        }

        composable("settings_snake_speed") {
            SnakeSpeedScreen(navController = navController)
        }

        composable("settings_music_vibration") {
            MusicVibrationControlScreen(navController = navController)
        }

        composable("settings_change_button_type") {
            ButtonTypeScreen(navController = navController)
        }

        composable("settings_language") {
            LanguageScreen(navController = navController)
        }

        composable("Exit") {
            exitProcess(0)
        }

        composable("snake_game_walls_selection") {
            WallsSelectionScreen(navController = navController)
        }

        composable(
            "snake_game_walls/{wallsLevel}",
            listOf(navArgument("wallsLevel") { NavType.IntType })
        ) {
            val level = it.arguments?.getString("wallsLevel")?.toInt() ?: 0
            Snake(
                settings = settings,
                gameType = GameTypeEnum.SNAKE_GAME_WALLS,
                navController = navController,
                buttonType = buttonType,
                level = level
            )
        }

        composable("snake_game_maze") {
            Snake(
                settings = settings,
                gameType = GameTypeEnum.SNAKE_GAME_MAZE,
                navController = navController,
                buttonType = buttonType
            )
        }

        composable("snake_game_speed") {
            Snake(
                settings = settings,
                gameType = GameTypeEnum.SNAKE_GAME_SPEED,
                navController = navController,
                buttonType = buttonType
            )
        }
    }
}