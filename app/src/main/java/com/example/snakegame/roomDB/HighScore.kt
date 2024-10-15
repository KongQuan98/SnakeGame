import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "highscores")
data class HighScoreEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val score: Int,
    val date: String
)
