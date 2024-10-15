//import android.content.Context
//import androidx.room.Room
//import com.example.snakegame.repo.HighScoreRepository
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object DatabaseModule {
//
//    @Provides
//    @Singleton
//    fun provideDatabase(@ApplicationContext context: Context): HighScoreDatabase {
//        return Room.databaseBuilder(
//            context,
//            HighScoreDatabase::class.java,
//            "highscore_database"
//        ).build()
//    }
//
//    @Provides
//    fun provideHighScoreDao(database: HighScoreDatabase): HighScoreDao {
//        return database.highScoreDao()
//    }
//
//    @Provides
//    @Singleton
//    fun provideRepository(highScoreDao: HighScoreDao): HighScoreRepository {
//        return HighScoreRepository(highScoreDao)
//    }
//}
