package com.example.snakegame.service.roomDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HighScoreDao {
    @Insert
    suspend fun insertHighScore(highScore: HighScoreEntity)

    @Query("SELECT * FROM highscores ORDER BY score DESC")
    suspend fun getAllHighScores(): List<HighScoreEntity>

    @Query("DELETE FROM highscores")
    suspend fun deleteAll()
}
