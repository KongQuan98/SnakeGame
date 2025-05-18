package com.example.snakegame.service.roomDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HighScoreDao {
    @Insert
    suspend fun insertHighScore(highScore: HighScoreEntity)

    @Insert
    suspend fun insertWallsHighScore(highScore: WallsHighScoreEntity)

    @Insert
    suspend fun insertSpeedHighScore(highScore: SpeedHighScoreEntity)

    @Query("SELECT * FROM highscores ORDER BY score DESC")
    suspend fun getAllHighScores(): List<HighScoreEntity>

    @Query("SELECT * FROM wallshighscores ORDER BY score DESC")
    suspend fun getAllWallsHighScores(): List<WallsHighScoreEntity>

    @Query("SELECT * FROM speedhighscores ORDER BY score DESC")
    suspend fun getAllSpeedHighScores(): List<SpeedHighScoreEntity>

    @Query("DELETE FROM highscores")
    suspend fun deleteClassicHighscore()

    @Query("DELETE FROM wallshighscores")
    suspend fun deleteWallsHighscore()

    @Query("DELETE FROM speedhighscores")
    suspend fun deleteSpeedHighscore()
}
