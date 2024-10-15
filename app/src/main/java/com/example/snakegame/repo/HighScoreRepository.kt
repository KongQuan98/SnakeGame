package com.example.snakegame.repo

import HighScoreDao
import HighScoreEntity
import com.example.snakegame.ui.screen.HighScore

class HighScoreRepository(private val highScoreDao: HighScoreDao) {
    suspend fun insert(highScore: HighScore) {
        highScoreDao.insertHighScore(convertModelToEntity(highScore))
    }

    suspend fun getAllHighScores(): List<HighScore> {
        return convertEntityToModel(highScoreDao.getAllHighScores())
    }

    suspend fun deleteAll() {
        highScoreDao.deleteAll()
    }

    private fun convertEntityToModel(highScoreEntity: List<HighScoreEntity>): List<HighScore> {
        val highScoreList = mutableListOf<HighScore>()
        with(highScoreEntity) {
            this.forEach {
                highScoreList.add(
                    HighScore(
                        name = it.name,
                        score = it.score,
                        date = it.date
                    )
                )
            }
        }

        return highScoreList
    }

    private fun convertModelToEntity(highScore: HighScore): HighScoreEntity =
        HighScoreEntity(
            name = highScore.name,
            score = highScore.score,
            date = highScore.date
        )
}
