package com.example.snakegame.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.snakegame.datamodel.GameTypeEnum
import com.example.snakegame.ui.screen.GameLogic
import com.example.snakegame.ui.screen.HighScoreScreen
import com.example.snakegame.ui.screen.MainMenu
import com.example.snakegame.ui.screen.Snake
import com.example.snakegame.ui.screen.SpecialModeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val context = LocalContext.current

    NavHost(navController = navController, startDestination = "main_menu") {
        // Main Menu Screen
        composable("main_menu") {
            MainMenu(navController)
        }

        // Snake Game Screen
        composable("snake_game") {
            // Use remember to instantiate Game with a CoroutineScope
            val scope = rememberCoroutineScope()
            val game =
                remember { GameLogic(scope = scope, context, GameTypeEnum.SNAKE_GAME_CLASSIC) }
            Snake(
                game = game,
                gameType = GameTypeEnum.SNAKE_GAME_CLASSIC,
                navController = navController
            )
        }

        // Add other screens like highscore, special mode, settings as needed
        composable("special_mode") {
            SpecialModeScreen(navController = navController)
        }

        composable("high_score") {
            HighScoreScreen(navController = navController)
        }

        composable("settings") {

        }

        composable("snake_game_walls") {
            val scope = rememberCoroutineScope()
            val game = remember { GameLogic(scope = scope, context, GameTypeEnum.SNAKE_GAME_WALLS) }
            Snake(
                game = game,
                gameType = GameTypeEnum.SNAKE_GAME_WALLS,
                navController = navController
            )
        }

        composable("snake_game_maze") {
            val scope = rememberCoroutineScope()
            val game = remember { GameLogic(scope = scope, context, GameTypeEnum.SNAKE_GAME_MAZE) }
            Snake(
                game = game,
                gameType = GameTypeEnum.SNAKE_GAME_MAZE,
                navController = navController
            )
        }

        composable("snake_game_speed") {
            val scope = rememberCoroutineScope()
            val game = remember { GameLogic(scope = scope, context, GameTypeEnum.SNAKE_GAME_SPEED) }
            Snake(
                game = game,
                gameType = GameTypeEnum.SNAKE_GAME_SPEED,
                navController = navController
            )
        }
    }
}