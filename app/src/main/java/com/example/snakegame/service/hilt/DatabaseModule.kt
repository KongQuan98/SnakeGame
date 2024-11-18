package com.example.snakegame.service.hilt

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.example.snakegame.service.repo.HighScoreRepository
import com.example.snakegame.service.roomDB.HighScoreDao
import com.example.snakegame.service.roomDB.HighScoreDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): HighScoreDatabase {
        return Room.databaseBuilder(
            context,
            HighScoreDatabase::class.java,
            "highscore_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideHighScoreDao(database: HighScoreDatabase): HighScoreDao {
        return database.highScoreDao()
    }

    @Singleton
    @Provides
    fun provideHighScoreRepository(highScoreDao: HighScoreDao): HighScoreRepository {
        return HighScoreRepository(highScoreDao)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.dataStoreFile("settings.preferences_pb") }
        )
    }
}
