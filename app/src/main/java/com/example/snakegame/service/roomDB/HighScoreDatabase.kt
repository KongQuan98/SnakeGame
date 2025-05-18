package com.example.snakegame.service.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        HighScoreEntity::class,
        WallsHighScoreEntity::class,
        SpeedHighScoreEntity::class,
    ], version = 2
)
abstract class HighScoreDatabase : RoomDatabase() {
    abstract fun highScoreDao(): HighScoreDao

    companion object {
        @Volatile
        private var INSTANCE: HighScoreDatabase? = null

        fun getDatabase(context: Context): HighScoreDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, HighScoreDatabase::class.java, "highscore_database"
                ).addMigrations(MIGRATION_1_2).build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS wallshighscores (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                score INTEGER NOT NULL,
                wallsLevel TEXT NOT NULL
            )
            """.trimIndent()
                )

                // Create speedhighscores tableH
                database.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS speedhighscores (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                score INTEGER NOT NULL,
                maxSpeedReached TEXT NOT NULL
            )
            """.trimIndent()
                )
            }
        }

    }
}
