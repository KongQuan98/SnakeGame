import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.snakegame.R
import com.example.snakegame.presentation.ui.theme.LightGreen
import com.example.snakegame.presentation.ui.utility.VibrationManager.vibrate

@Composable
fun GameOverDialog(
    score: Int,
    onReplay: () -> Unit,
    onBackToMainMenu: () -> Unit,
    onSeeHighScore: () -> Unit,
    onDismiss: () -> Unit,
    isPaused: Boolean,
    onContinue: () -> Unit
) {
    val context = LocalContext.current

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = LightGreen,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Game Over Title
                Text(
                    text = if (isPaused) "Game Paused" else "Game Over!",
                    fontFamily = FontFamily(
                        Font(R.font.nokia_font)
                    ),
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Show the player's score
                Text(
                    text = "Your Score: $score",
                    fontFamily = FontFamily(
                        Font(R.font.nokia_font)
                    ),
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Pause Button
                if (isPaused) {
                    Button(
                        onClick = {
                            onContinue()
                            vibrate(context)
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                    ) {
                        Text(
                            text = "Continue",
                            fontFamily = FontFamily(
                                Font(R.font.nokia_font)
                            ),
                            color = LightGreen,
                        )
                    }
                }

                // Replay Button
                Button(
                    onClick = {
                        onReplay()
                        vibrate(context)
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                ) {
                    Text(
                        text = "Replay",
                        fontFamily = FontFamily(
                            Font(R.font.nokia_font)
                        ),
                        color = LightGreen,
                    )
                }

                // Back to Main Menu Button
                Button(
                    onClick = {
                        onBackToMainMenu()
                        vibrate(context)
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                ) {
                    Text(
                        text = "Back to Main Menu",
                        fontFamily = FontFamily(
                            Font(R.font.nokia_font)
                        ),
                        color = LightGreen,
                    )
                }

                // See High Score Button
                Button(
                    onClick = {
                        onSeeHighScore()
                        vibrate(context)
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                ) {
                    Text(
                        text = "See High Score",
                        fontFamily = FontFamily(
                            Font(R.font.nokia_font)
                        ),
                        color = LightGreen,
                    )
                }
            }
        }
    }

}
