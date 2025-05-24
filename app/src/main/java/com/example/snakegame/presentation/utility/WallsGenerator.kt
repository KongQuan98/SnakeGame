package com.example.snakegame.presentation.utility

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
                listOf(
                    Pair(i, 0),
                    Pair(i, GameLogic.BOARD_SIZE - 1),
                    Pair(0, i),
                    Pair(GameLogic.BOARD_SIZE - 1, i)
                )
            }
        }

        2 -> {
            // Level 3: Surrounding walls with a box in the middle
            val middleX = GameLogic.BOARD_SIZE / 2
            val middleY = GameLogic.BOARD_SIZE / 2
            (0 until GameLogic.BOARD_SIZE).flatMap { i ->
                listOf(
                    Pair(i, 0),
                    Pair(i, GameLogic.BOARD_SIZE - 1),
                    Pair(0, i),
                    Pair(GameLogic.BOARD_SIZE - 1, i)
                )
            } + listOf(
                Pair(middleX - 2, middleY - 2),
                Pair(middleX - 2, middleY + 2),
                Pair(middleX + 2, middleY - 2),
                Pair(middleX + 2, middleY + 2)
            )
        }

        3 -> {
            // Level 4: No surrounding walls, middle box pattern
            val middleX = GameLogic.BOARD_SIZE / 2
            val middleY = GameLogic.BOARD_SIZE / 2
            (0 until GameLogic.BOARD_SIZE).flatMap { i ->
                listOf(
                    Pair(i, 0),
                    Pair(i, GameLogic.BOARD_SIZE - 1),
                    Pair(0, i),
                    Pair(GameLogic.BOARD_SIZE - 1, i)
                )
            } + listOf(
                Pair(middleX - 2, middleY - 2),
                Pair(middleX - 2, middleY + 2),
                Pair(middleX + 2, middleY - 2),
                Pair(middleX + 2, middleY + 2)
            ) + (middleY - 2..middleY + 2).flatMap { y ->
                listOf(Pair(middleX - 2, y), Pair(middleX + 2, y))
            }
        }

        4 -> {
            // Level 2: Surrounding walls with a middle '=' pattern
            val middleY = GameLogic.BOARD_SIZE / 2
            (0 until GameLogic.BOARD_SIZE).flatMap { i ->
                listOf(
                    Pair(i, 0),
                    Pair(i, GameLogic.BOARD_SIZE - 1),
                    Pair(0, i),
                    Pair(GameLogic.BOARD_SIZE - 1, i)
                )
            } + (2..GameLogic.BOARD_SIZE - 3).map { Pair(it, middleY) }
        }

        5 -> {
            // Level 5: Zigzag pattern
            (0 until GameLogic.BOARD_SIZE).flatMap { i ->
                if (i % 2 == 0) listOf(
                    Pair(i, i), Pair(GameLogic.BOARD_SIZE - 1 - i, i)
                ) else emptyList()
            }
        }

        6 -> {
            val size = GameLogic.BOARD_SIZE

            // Outer border walls
            val border = (0 until size).flatMap { i ->
                listOf(
                    Pair(i, 0), Pair(i, size - 1), Pair(0, i), Pair(size - 1, i)
                )
            }

            // Hardcoded scattered inner wall tiles (excluding borders)
            val scatteredWalls = listOf(
                Pair(2, 3),
                Pair(3, 5),
                Pair(5, 2),
                Pair(6, 9),
                Pair(7, 4),
                Pair(8, 6),
                Pair(9, 3),
                Pair(11, 5),
                Pair(12, 9),
                Pair(13, 2),
                Pair(14, 4),
                Pair(15, 6),
                Pair(16, 8),
                Pair(17, 3),
                Pair(3, 12),
                Pair(5, 14),
                Pair(7, 16),
                Pair(9, 13),
                Pair(11, 15),
                Pair(13, 17),
                Pair(15, 11),
                Pair(17, 13)
            )

            border + scatteredWalls
        }

        7 -> {
            // Level 7: Vertical bars without surrounding walls
//            val middleX = GameLogic.BOARD_SIZE / 2
            (2 until GameLogic.BOARD_SIZE step 4).flatMap { x ->
                (1..GameLogic.BOARD_SIZE - 2).map { y -> Pair(y, x) }
            } + (2 until GameLogic.BOARD_SIZE step 4).flatMap { x ->
                (1..GameLogic.BOARD_SIZE - 2).map { y -> Pair(y, GameLogic.BOARD_SIZE - x - 1) }
            }
        }

        8 -> {
            val size = GameLogic.BOARD_SIZE

            val topFree = 3
            val bottomFree = 3
            val leftFree = 3
            val rightFree = 3

            val walls = mutableListOf<Pair<Int, Int>>()

            for (row in topFree until size - bottomFree) {
                for (col in leftFree until size - rightFree) {
                    if (col != 7) {
                        walls.add(Pair(row, col))
                    }
                }
            }

            walls
        }


        9 -> {
            // Level 8: Checkerboard pattern
            (0 until GameLogic.BOARD_SIZE step 2).flatMap { x ->
                (0 until GameLogic.BOARD_SIZE step 2).map { y -> Pair(x, y) }
            }
        }

        else -> emptyList()
    }
}
