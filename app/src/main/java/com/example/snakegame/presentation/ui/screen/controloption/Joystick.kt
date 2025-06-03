package com.example.snakegame.presentation.ui.screen.controloption

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.snakegame.presentation.ui.theme.DarkGreen
import com.example.snakegame.presentation.ui.theme.LightGreen
import kotlin.math.abs
import kotlin.math.min


/**
 * A Joystick composable that draws a base circle and a movable hat.
 *
 * It converts drag gestures into one of the four cardinal directions:
 * Up (0, -1), Down (0, 1), Left (-1, 0), or Right (1, 0).
 *
 * @param modifier Modifier to style and position this composable.
 * @param onDirectionChange A callback that receives a Pair<Int,Int> indicating the direction.
 */
@Composable
fun Joystick(
    modifier: Modifier = Modifier,
    sizeDp: Float = 150f, // New parameter
    onDirectionChange: (Pair<Int, Int>) -> Unit
) {
    var joystickOffset by remember { mutableStateOf(Offset.Zero) }
    var sizeState by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .size(sizeDp.dp) // Use the sizeDp here
            .onSizeChanged { sizeState = it }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { _, dragAmount ->
                        val newOffset = joystickOffset + dragAmount
                        val maxRadius = min(sizeState.width, sizeState.height) / 3f
                        val distance = newOffset.getDistance()

                        joystickOffset = if (distance <= maxRadius) {
                            newOffset
                        } else {
                            newOffset * (maxRadius / distance)
                        }

                        val normX = joystickOffset.x / maxRadius
                        val normY = joystickOffset.y / maxRadius

                        val threshold = 0.5f
                        val direction = if (abs(normX) > abs(normY)) {
                            when {
                                normX > threshold -> Pair(1, 0)
                                normX < -threshold -> Pair(-1, 0)
                                else -> Pair(0, 0)
                            }
                        } else {
                            when {
                                normY > threshold -> Pair(0, 1)
                                normY < -threshold -> Pair(0, -1)
                                else -> Pair(0, 0)
                            }
                        }

                        onDirectionChange(direction)
                    },
                    onDragEnd = {
                        joystickOffset = Offset.Zero
                        onDirectionChange(Pair(0, 0))
                    },
                    onDragCancel = {
                        joystickOffset = Offset.Zero
                        onDirectionChange(Pair(0, 0))
                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val center = Offset(canvasWidth / 2f, canvasHeight / 2f)
            val baseRadius = min(canvasWidth, canvasHeight) / 3f
            val hatRadius = min(canvasWidth, canvasHeight) / 6f

            drawCircle(
                color = LightGreen,
                radius = baseRadius,
                center = center
            )
            drawCircle(
                color = DarkGreen,
                radius = hatRadius,
                center = center + joystickOffset
            )
        }
    }
}
