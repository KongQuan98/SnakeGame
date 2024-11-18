package com.example.snakegame.presentation.ui.utility

import com.example.snakegame.presentation.ui.screen.GameLogic

// function to generate walls
fun generateWalls(): List<Pair<Int, Int>> {
    val walls = mutableListOf<Pair<Int, Int>>()

    // Add top and bottom walls
    for (i in 0 until GameLogic.BOARD_SIZE) {
        walls.add(Pair(i, 0)) // Top wall
        walls.add(Pair(i, GameLogic.BOARD_SIZE - 1)) // Bottom wall
    }

    // Add left and right walls
    for (i in 0 until GameLogic.BOARD_SIZE) {
        walls.add(Pair(0, i)) // Left wall
        walls.add(Pair(GameLogic.BOARD_SIZE - 1, i)) // Right wall
    }

    return walls
}

fun generateWallsForMaze(level: Int): List<Pair<Int, Int>> {
    return when (level) {
        1 -> {
            // Level 1: Surrounding walls
            (0 until GameLogic.BOARD_SIZE).flatMap { i ->
                listOf(Pair(i, 0), Pair(i, GameLogic.BOARD_SIZE - 1), Pair(0, i), Pair(GameLogic.BOARD_SIZE - 1, i))
            }
        }
        2 -> {
            // Level 3: Surrounding walls with a box in the middle
            val middleX = GameLogic.BOARD_SIZE / 2
            val middleY = GameLogic.BOARD_SIZE / 2
            (0 until GameLogic.BOARD_SIZE).flatMap { i ->
                listOf(Pair(i, 0), Pair(i, GameLogic.BOARD_SIZE - 1), Pair(0, i), Pair(GameLogic.BOARD_SIZE - 1, i))
            } + listOf(
                Pair(middleX - 2, middleY - 2), Pair(middleX - 2, middleY + 2),
                Pair(middleX + 2, middleY - 2), Pair(middleX + 2, middleY + 2)
            )
        }
        3 -> {
            // Level 4: No surrounding walls, middle box pattern
            val middleX = GameLogic.BOARD_SIZE / 2
            val middleY = GameLogic.BOARD_SIZE / 2
            listOf(
                Pair(middleX - 2, middleY - 2), Pair(middleX - 2, middleY + 2),
                Pair(middleX + 2, middleY - 2), Pair(middleX + 2, middleY + 2)
            ) + (middleY - 2..middleY + 2).flatMap { y ->
                listOf(Pair(middleX - 2, y), Pair(middleX + 2, y))
            }
        }
        4 -> {
            // Level 2: Surrounding walls with a middle '=' pattern
            val middleY = GameLogic.BOARD_SIZE / 2
            (0 until GameLogic.BOARD_SIZE).flatMap { i ->
                listOf(Pair(i, 0), Pair(i, GameLogic.BOARD_SIZE - 1), Pair(0, i), Pair(GameLogic.BOARD_SIZE - 1, i))
            } + (2..GameLogic.BOARD_SIZE - 3).map { Pair(it, middleY) }
        }
        5 -> {
            // Level 5: Zigzag pattern
            (0 until GameLogic.BOARD_SIZE).flatMap { i ->
                if (i % 2 == 0) listOf(Pair(i, i), Pair(GameLogic.BOARD_SIZE - 1 - i, i)) else emptyList()
            }
        }
        6 -> {
            // Level 6: Two horizontal bars
            val upperY = GameLogic.BOARD_SIZE / 4
            val lowerY = 3 * GameLogic.BOARD_SIZE / 4
            (2..GameLogic.BOARD_SIZE - 3).flatMap { x ->
                listOf(Pair(x, upperY), Pair(x, lowerY))
            }
        }
        7 -> {
            // Level 7: Vertical bars without surrounding walls
//            val middleX = GameLogic.BOARD_SIZE / 2
            (2 until GameLogic.BOARD_SIZE step 4).flatMap { x ->
                (1..GameLogic.BOARD_SIZE - 2).map { y -> Pair(x, y) }
            } + (2 until GameLogic.BOARD_SIZE step 4).flatMap { x ->
                (1..GameLogic.BOARD_SIZE - 2).map { y -> Pair(GameLogic.BOARD_SIZE - x - 1, y) }
            }
        }
        8 -> {
            // Level 8: Checkerboard pattern
            (0 until GameLogic.BOARD_SIZE step 2).flatMap { x ->
                (0 until GameLogic.BOARD_SIZE step 2).map { y -> Pair(x, y) }
            }
        }
        9 -> {
            // Level 9: Diagonal walls
            (0 until GameLogic.BOARD_SIZE).map { i -> Pair(i, i) } + (0 until GameLogic.BOARD_SIZE).map { i -> Pair(i, GameLogic.BOARD_SIZE - i - 1) }
        }
        10 -> {
            // Level 10: Complex maze with various patterns
            (0 until GameLogic.BOARD_SIZE).flatMap { i ->
                listOf(Pair(i, 0), Pair(i, GameLogic.BOARD_SIZE - 1), Pair(0, i), Pair(GameLogic.BOARD_SIZE - 1, i))
            } + (2 until GameLogic.BOARD_SIZE - 2 step 4).flatMap { i ->
                (2 until GameLogic.BOARD_SIZE - 2 step 4).map { j -> Pair(i, j) }
            }
        }
        else -> emptyList()
    }
}
