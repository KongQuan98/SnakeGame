import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.snakegame.R
import com.example.snakegame.presentation.datamodel.GameTypeEnum
import com.example.snakegame.presentation.ui.theme.LightGreen
import com.example.snakegame.presentation.utility.VibrationManager.vibrate

@Composable
fun GameOverDialog(
    score: Int,
    onReplay: () -> Unit,
    onBackToMainMenu: () -> Unit,
    onSeeHighScore: () -> Unit,
    onDismiss: () -> Unit,
    isPaused: Boolean,
    gameType: GameTypeEnum = GameTypeEnum.SNAKE_GAME_CLASSIC,
    onBackToSpecialGameMenu: () -> Unit,
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
                    text = if (isPaused) stringResource(id = R.string.game_paused) else stringResource(
                        id = R.string.game_over
                    ),
                    fontFamily = FontFamily(
                        Font(R.font.nokia_font)
                    ),
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Show the player's score
                Text(
                    text = "${stringResource(id = R.string.your_score_is)}: $score",
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
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                    ) {
                        Text(
                            text = stringResource(id = R.string.continue_text),
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
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                ) {
                    Text(
                        text = stringResource(id = R.string.replay),
                        fontFamily = FontFamily(
                            Font(R.font.nokia_font)
                        ),
                        color = LightGreen,
                    )
                }

                if (gameType == GameTypeEnum.SNAKE_GAME_WALLS || gameType == GameTypeEnum.SNAKE_GAME_SPEED) {
                    val buttonText = when {
                        gameType == GameTypeEnum.SNAKE_GAME_WALLS -> stringResource(id = R.string.walls_selection)
                        else -> stringResource(id = R.string.back_to_special_mode)
                    }
                    Button(
                        onClick = {
                            onBackToSpecialGameMenu()
                            vibrate(context)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            text = buttonText,
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily(
                                Font(R.font.nokia_font)
                            ),
                            color = LightGreen,
                        )
                    }
                }

                // Back to Main Menu Button
                Button(
                    onClick = {
                        onBackToMainMenu()
                        vibrate(context)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                ) {
                    Text(
                        text = stringResource(id = R.string.back_to_main_menu),
                        textAlign = TextAlign.Center,
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
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                ) {
                    Text(
                        text = stringResource(id = R.string.see_high_score),
                        textAlign = TextAlign.Center,
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
