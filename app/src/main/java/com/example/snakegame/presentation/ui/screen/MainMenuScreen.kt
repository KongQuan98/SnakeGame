package com.example.snakegame.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.snakegame.R
import com.example.snakegame.presentation.ui.theme.DarkGreen
import com.example.snakegame.presentation.ui.theme.LightGreen
import com.example.snakegame.presentation.ui.utility.buildCompleteSnakePath
import com.example.snakegame.presentation.ui.utility.vibrate
import kotlinx.coroutines.delay
import kotlin.system.exitProcess


@Composable
fun MainMenu(navController: NavController) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    val menuOptions = listOf(
        "Start Game" to "snake_game",
        "Special Mode" to "special_mode",
        "High Scores" to "high_score",
        "Settings" to "settings",
        "Exit" to null // Null as we don't need navigation for exit
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
                text = "Retro Snake",
                color = Color.Black,
                fontFamily = FontFamily(
                    Font(R.font.nokia_font)
                ),
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
            )

            Column(
                Modifier
                    .padding(40.dp)
                    .border(2.dp, Color.Black), // Border for the main menu
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Menu Options
                menuOptions.forEachIndexed { index, option ->
                    MenuOption(text = option.first, isSelected = index == selectedIndex, onClick = {
                        vibrate(context)
                        selectedIndex = index
                        option.second?.let { route ->
                            navController.navigate(route)
                        } ?: run {
                            exitGame() // Handle exit logic
                        }
                    })
                }

            }
        }

    }
}

@Composable
fun MenuOption(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isSelected) Color.Black else LightGreen)
            .padding(8.dp)
            .clickable(onClick = onClick), contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) LightGreen else Color.Black,
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.nokia_font)),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SnakeAnimation() {
    // Define the size of the snake
    val snakeLength = 50
    val snakeSize = 16.dp

    // State variables for x and y positions
    val snakePositions = remember { mutableStateListOf<Pair<Float, Float>>() }

    // Define the movement bounds (screen borders)
    val (screenWidth, screenHeight) = getDeviceDimensions()
    val padding = 16f

    // Snake movement path: list of positions covering the top, right, bottom, and left borders
    val fullPath = buildCompleteSnakePath(screenWidth, screenHeight, padding)

    // Animation for the snake moving
    LaunchedEffect(Unit) {
        // Initialize the snake position along the top edge
        snakePositions.addAll(fullPath.take(snakeLength))

        // Infinite loop to keep the snake circulating
        while (true) {
            delay(10L) // Delay between snake's position updates

            // Move the snake along the path
            val currentHeadIndex = fullPath.indexOf(snakePositions.first())
            val nextIndex = (currentHeadIndex + 1) % fullPath.size

            snakePositions.add(0, fullPath[nextIndex])

            if (snakePositions.size > snakeLength) {
                snakePositions.removeLast()
            }
        }
    }

    // Draw the snake
    Box(Modifier.fillMaxSize()) {
        snakePositions.forEach { (x, y) ->
            Box(
                Modifier
                    .offset(x.dp, y.dp)
                    .size(snakeSize)
                    .background(DarkGreen, RoundedCornerShape(4.dp)) // Green squares for snake
            )
        }
    }
}

@Composable
fun getDeviceDimensions(): Pair<Float, Float> {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.toFloat() // Screen width in dp
    val screenHeight = configuration.screenHeightDp.toFloat() // Screen height in dp

    return Pair(screenWidth, screenHeight)
}

fun exitGame() {
    // Implement exit logic here
    exitProcess(0)
}