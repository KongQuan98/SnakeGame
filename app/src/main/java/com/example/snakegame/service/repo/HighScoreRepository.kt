package com.example.snakegame.service.repo

import com.example.snakegame.presentation.ui.screen.HighScore
import com.example.snakegame.service.roomDB.HighScoreDao
import com.example.snakegame.service.roomDB.HighScoreEntity
import com.example.snakegame.service.roomDB.SpeedHighScoreEntity
import com.example.snakegame.service.roomDB.WallsHighScoreEntity

class HighScoreRepository(private val highScoreDao: HighScoreDao) {
    suspend fun insert(highScore: HighScore) {
        highScoreDao.insertHighScore(convertModelToHighScoreEntity(highScore))
    }

    suspend fun getAllHighScores(): List<HighScore> {
        return convertEntityToModel(highScoreEntity = highScoreDao.getAllHighScores())
    }

    suspend fun insertWallsHighScore(highScore: HighScore) {
        highScoreDao.insertWallsHighScore(convertModelToWallsHighScoreEntity(highScore))
    }

    suspend fun getWallsHighScores(): List<HighScore> {
        return convertEntityToModel(wallsHighScoreEntity = highScoreDao.getAllWallsHighScores())
    }

    suspend fun insertSpeedHighScore(highScore: HighScore) {
        highScoreDao.insertSpeedHighScore(convertModelToSpeedHighScoreEntity(highScore))
    }

    suspend fun getSpeedHighScores(): List<HighScore> {
        return convertEntityToModel(speedHighScoreEntity = highScoreDao.getAllSpeedHighScores())
    }

    suspend fun deleteAll() {
        highScoreDao.deleteClassicHighscore()
        highScoreDao.deleteSpeedHighscore()
        highScoreDao.deleteWallsHighscore()
    }

    private fun convertEntityToModel(
        highScoreEntity: List<HighScoreEntity>? = null,
        wallsHighScoreEntity: List<WallsHighScoreEntity>? = null,
        speedHighScoreEntity: List<SpeedHighScoreEntity>? = null
    ): List<HighScore> {
        val highScoreList = mutableListOf<HighScore>()

        highScoreEntity?.let { entity ->
            entity.forEach { classicHighScore ->
                highScoreList.add(
                    HighScore(
                        name = classicHighScore.name,
                        score = classicHighScore.score,
                    )
                )
            }
        }

        wallsHighScoreEntity?.let { entity ->
            entity.forEach { wallsHighScore ->
                highScoreList.add(
                    HighScore(
                        name = wallsHighScore.name,
                        score = wallsHighScore.score,
                        wallsLevel = wallsHighScore.wallsLevel
                    )
                )
            }
        }

        speedHighScoreEntity?.let { entity ->
            entity.forEach { speedHighScore ->
                highScoreList.add(
                    HighScore(
                        name = speedHighScore.name,
                        score = speedHighScore.score,
                        maxSpeedReached = speedHighScore.maxSpeedReached
                    )
                )
            }
        }

        return highScoreList
    }

    private fun convertModelToHighScoreEntity(highScore: HighScore): HighScoreEntity =
        HighScoreEntity(
            name = highScore.name,
            score = highScore.score,
        )

    private fun convertModelToWallsHighScoreEntity(highScore: HighScore): WallsHighScoreEntity =
        WallsHighScoreEntity(
            name = highScore.name,
            score = highScore.score,
            wallsLevel = highScore.wallsLevel ?: ""
        )

    private fun convertModelToSpeedHighScoreEntity(highScore: HighScore): SpeedHighScoreEntity =
        SpeedHighScoreEntity(
            name = highScore.name,
            score = highScore.score,
            maxSpeedReached = highScore.maxSpeedReached ?: ""
        )
}
