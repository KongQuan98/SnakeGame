package com.example.snakegame.ui.utility

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.example.snakegame.ui.screen.GameLogic.Companion.BOARD_SIZE
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// vibrate for onclick
fun vibrate(context: Context, vibrationLevel: Long = 50) {
    val vibrator = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    vibrator.vibrate(
        VibrationEffect.createOneShot(
            vibrationLevel,
            VibrationEffect.DEFAULT_AMPLITUDE
        )
    )
}

// snake animation path
fun buildCompleteSnakePath(
    screenWidth: Float,
    screenHeight: Float,
    padding: Float
): List<Pair<Float, Float>> {
    val path = mutableListOf<Pair<Float, Float>>()

    // Top edge (left to right)
    var x = padding - 8
    while (x < screenWidth - padding - 6) {
        path.add(Pair(x, padding)) // Move along the top edge
        x += 8f
    }

    // Right edge (top to bottom)
    var y = padding
    while (y <= screenHeight - padding - 16) {
        path.add(Pair(screenWidth - padding - 6, y)) // Move down the right edge
        y += 8f
    }

    // Bottom edge (right to left)
    x = screenWidth - padding - 6
    while (x >= padding) {
        path.add(Pair(x, screenHeight - padding - 16)) // Move along the bottom edge
        x -= 8f
    }

    // Left edge (bottom to top)
    y = screenHeight - padding - 16
    while (y >= padding) {
        path.add(Pair(padding - 8, y)) // Move up the left edge
        y -= 8f
    }

    return path
}

//get current date
fun getCurrentDate(): String {
    val currentDate = LocalDate.now() // Get the current date
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // Define a format (optional)
    return currentDate.format(formatter) // Format and return the date as a String
}

// function to generate walls
fun generateWalls(): List<Pair<Int, Int>> {
    val walls = mutableListOf<Pair<Int, Int>>()

    // Add top and bottom walls
    for (i in 0 until BOARD_SIZE) {
        walls.add(Pair(i, 0)) // Top wall
        walls.add(Pair(i, BOARD_SIZE - 1)) // Bottom wall
    }

    // Add left and right walls
    for (i in 0 until BOARD_SIZE) {
        walls.add(Pair(0, i)) // Left wall
        walls.add(Pair(BOARD_SIZE - 1, i)) // Right wall
    }

    return walls
}

// Function to generate random food, excluding snake and wall positions
fun generateRandomFood(snake: List<Pair<Int, Int>>, walls: List<Pair<Int, Int>>): Pair<Int, Int> {
    // Create a list of all possible positions on the board
    val allPositions = mutableListOf<Pair<Int, Int>>()

    // Add all valid board positions to the list
    for (x in 0 until BOARD_SIZE) {
        for (y in 0 until BOARD_SIZE) {
            val position = Pair(x, y)
            // Only add positions that are not occupied by the snake or walls
            if (!snake.contains(position) && !walls.contains(position)) {
                allPositions.add(position)
            }
        }
    }

    // Pick a random position from the valid ones
    return if (allPositions.isNotEmpty()) {
        allPositions.random()
    } else {
        // In case no valid positions remain, this should not happen unless the entire board is filled
        Pair(0, 0)
    }
}