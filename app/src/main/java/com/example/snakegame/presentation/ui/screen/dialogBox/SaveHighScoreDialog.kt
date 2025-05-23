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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.snakegame.R
import com.example.snakegame.presentation.ui.theme.LightGreen
import com.example.snakegame.presentation.utility.VibrationManager.vibrate

@Composable
fun SaveHighScoreDialog(
    title: String,
    subtitle: String,
    onSubmit: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current

    // State to hold the player's name input
    var playerName by remember { mutableStateOf(TextFieldValue("")) }
    var isError by remember { mutableStateOf(false) }

    // Show dialog only if onDismiss has not been triggered
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
                // Congratulation Title
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(
                        Font(R.font.nokia_font)
                    ),
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Show the player's high score
                Text(
                    text = subtitle,
                    textAlign = TextAlign.Center,
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
                    label = { Text(stringResource(id = R.string.enter_your_name_here)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Black),
                    singleLine = true,
                    isError = isError // Show error if blank
                )

                // Display error message if name is empty
                if (isError) {
                    Text(
                        text = stringResource(id = R.string.name_cannot_be_empty),
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }

                // Save button
                Button(
                    onClick = {
                        if (playerName.text.isNotBlank()) {
                            onSubmit(playerName.text)
                            vibrate(context)
                        } else {
                            isError = true // Show error if name is empty
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                ) {
                    Text(
                        text = stringResource(id = R.string.save),
                        fontFamily = FontFamily(
                            Font(R.font.nokia_font)
                        ),
                        color = LightGreen,
                    )
                }

                // No Thanks button
                Button(
                    onClick = {
                        onDismiss()
                        vibrate(context)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = stringResource(id = R.string.no_thanks),
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

