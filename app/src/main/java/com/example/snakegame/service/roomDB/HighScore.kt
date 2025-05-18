package com.example.snakegame.service.roomDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "highscores")
data class HighScoreEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val score: Int,
)

@Entity(tableName = "wallshighscores")
data class WallsHighScoreEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val score: Int,
    val wallsLevel: String
)

@Entity(tableName = "speedhighscores")
data class SpeedHighScoreEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val score: Int,
    val maxSpeedReached: String
)
