package com.example.snakegame.presentation.ui.screen.controloption

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.snakegame.presentation.ui.theme.LightGreen
import com.example.snakegame.presentation.ui.utility.vibrate


@Composable
fun ArrowButtons(modifier: Modifier = Modifier, onDirectionChange: (Pair<Int, Int>) -> Unit) {
    val buttonSize = Modifier
        .size(64.dp)
    val context = LocalContext.current
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.padding(24.dp)) {
        Button(
            onClick = {
                onDirectionChange(Pair(0, -1))
                vibrate(context)
            },
            modifier = buttonSize,
            colors = ButtonDefaults.buttonColors(backgroundColor = LightGreen)
        ) {
            Icon(Icons.Default.KeyboardArrowUp, null)
        }

        Spacer(modifier = Modifier.height(12.dp)) // Add gap between rows

        Row {
            Button(
                onClick = {
                    onDirectionChange(Pair(-1, 0))
                    vibrate(context)
                },
                modifier = buttonSize,
                colors = ButtonDefaults.buttonColors(backgroundColor = LightGreen)
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, null)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Spacer(modifier = buttonSize)
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                onClick = {
                    onDirectionChange(Pair(1, 0))
                    vibrate(context)
                },
                modifier = buttonSize,
                colors = ButtonDefaults.buttonColors(backgroundColor = LightGreen)
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null)
            }
        }

        Spacer(modifier = Modifier.height(12.dp)) // Add gap between rows

        Button(
            onClick = {
                onDirectionChange(Pair(0, 1))
                vibrate(context)
            },
            modifier = buttonSize,
            colors = ButtonDefaults.buttonColors(backgroundColor = LightGreen)
        ) {
            Icon(Icons.Default.KeyboardArrowDown, null)
        }
    }
}
