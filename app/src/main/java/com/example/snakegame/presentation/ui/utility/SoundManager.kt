import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.snakegame.R

class SoundManager(context: Context) {
    private val soundPool: SoundPool
    private val soundIds: HashMap<String, Int> = HashMap()

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5) // Number of simultaneous sounds
            .setAudioAttributes(audioAttributes)
            .build()

        // Load your sounds into the SoundPool
        soundIds["eat"] = soundPool.load(context, R.raw.eat_sound, 1)
        soundIds["bonk_body"] = soundPool.load(context, R.raw.bonk_body, 1)
        soundIds["bonk_wall"] = soundPool.load(context, R.raw.bonk_wall, 1)
        soundIds["big_food"] = soundPool.load(context, R.raw.big_food_ding, 1)
    }

    // Play sound by name
    fun playSound(soundName: String) {
        val soundId = soundIds[soundName]
        if (soundId != null) {
            soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
        }
    }

    // Release resources when done
    fun release() {
        soundPool.release()
    }
}
