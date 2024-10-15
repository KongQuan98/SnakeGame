import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snakegame.ui.screen.HighScore
import com.example.snakegame.repo.HighScoreRepository
import kotlinx.coroutines.launch

class HighScoreViewModel(private val repository: HighScoreRepository) : ViewModel() {

    fun addHighScore(name: String, score: Int, date: String) {
        viewModelScope.launch {
            repository.insert(HighScore(name = name, score = score, date = date))
        }
    }

    suspend fun getHighScores(): List<HighScore> {
        return repository.getAllHighScores()
    }

    suspend fun getHighestScores(): HighScore? {
        val list = repository.getAllHighScores()
        return list.maxByOrNull { it.score }
    }
}
