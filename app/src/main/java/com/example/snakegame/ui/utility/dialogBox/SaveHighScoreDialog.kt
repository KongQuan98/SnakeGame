import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.snakegame.R
import com.example.snakegame.ui.theme.LightGreen
import com.example.snakegame.ui.utility.vibrate

@Composable
fun SaveHighScoreDialog(
    score: Int,
    onSubmit: (String) -> Unit
) {
    val context = LocalContext.current

    // State to hold the player's name input
    var playerName by remember { mutableStateOf(TextFieldValue("")) }
    var isError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { }) {
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
                // Congratulation Title
                Text(
                    text = "Congratulations!",
                    fontFamily = FontFamily(
                        Font(R.font.nokia_font)
                    ),
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Show the player's high score
                Text(
                    text = "New High Score: $score",
                    fontFamily = FontFamily(
                        Font(R.font.nokia_font)
                    ),
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // TextField for user to enter their name
                TextField(
                    value = playerName,
                    onValueChange = { newText ->
                        playerName = newText
                        isError = newText.text.isBlank() // Check if input is blank
                    },
                    label = { Text("Enter your name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Black),
                    isError = isError // Show error if blank
                )

                // Display error message if name is empty
                if (isError) {
                    Text(
                        text = "Name cannot be empty",
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }

                // Back to Main Menu Button
                Button(
                    onClick = {
                        onSubmit(playerName.text)
                        vibrate(context)
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                ) {
                    Text(
                        text = "Save",
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
