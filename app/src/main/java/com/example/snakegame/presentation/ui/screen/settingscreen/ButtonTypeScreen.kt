package com.example.snakegame.presentation.ui.screen.settingscreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.snakegame.R
import com.example.snakegame.presentation.datamodel.ButtonTypeEnum
import com.example.snakegame.presentation.ui.screen.MenuOption
import com.example.snakegame.presentation.ui.screen.SnakeAnimation
import com.example.snakegame.presentation.ui.screen.controloption.ArrowButtons
import com.example.snakegame.presentation.ui.screen.controloption.Joystick
import com.example.snakegame.presentation.ui.theme.DarkGreen
import com.example.snakegame.presentation.ui.theme.LightGreen
import com.example.snakegame.presentation.utility.ClickDebouncer
import com.example.snakegame.presentation.utility.VibrationManager.vibrate
import com.example.snakegame.presentation.viewmodel.SettingsViewModel

@Composable
fun ButtonTypeScreen(navController: NavController) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    var selectedButtonType by remember { mutableStateOf(ButtonTypeEnum.ARROW_BUTTON) }
    val scrollState = rememberScrollState()

    val menuOptions = listOf(
        stringResource(id = R.string.arrow_button) to ButtonTypeEnum.ARROW_BUTTON,
        stringResource(id = R.string.joystick) to ButtonTypeEnum.JOYSTICK,
    )
    val context = LocalContext.current

    val viewModel: SettingsViewModel = hiltViewModel()
    val buttonType by viewModel.buttonType.observeAsState(initial = ButtonTypeEnum.ARROW_BUTTON)
    val joyStickSize by viewModel.joyStickSize.observeAsState(initial = 150f)

    selectedButtonType = buttonType

    menuOptions.forEachIndexed { index, pair ->
        if (pair.second == buttonType) {
            selectedIndex = index
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(LightGreen), // Dark background to mimic old phone UI
        contentAlignment = Alignment.Center
    ) {

        SnakeAnimation()

        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(top = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                modifier = Modifier.padding(horizontal = 40.dp),
                text = stringResource(id = R.string.change_button_type),
                color = Color.Black,
                fontFamily = FontFamily(
                    Font(R.font.nokia_font)
                ),
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 45.sp
            )

            Box(
                Modifier
                    .padding(start = 40.dp, end = 40.dp, top = 10.dp, bottom = 10.dp)
                    .wrapContentHeight()
                    .width(300.dp)
                    .background(DarkGreen)
            ) {
                when (selectedButtonType) {
                    ButtonTypeEnum.ARROW_BUTTON -> ArrowButtons(Modifier.align(Alignment.Center)) {
                        Unit
                    }

                    ButtonTypeEnum.JOYSTICK -> Joystick(
                        Modifier
                            .height(250.dp)
                            .align(Alignment.Center),
                        sizeDp = joyStickSize
                    ) {
                        Unit
                    }
                }
            }

            when (selectedButtonType) {
                ButtonTypeEnum.ARROW_BUTTON -> {
                    /* Nothing for now **/
                }

                else -> {
                    Slider(
                        value = joyStickSize,
                        onValueChange = {
                            viewModel.updateJoyStickSize(it)
                        },
                        valueRange = 100f..300f,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 40.dp)
                    )
                }
            }

            Column(
                Modifier
                    .padding(top = 10.dp, start = 40.dp, end = 40.dp, bottom = 40.dp)
                    .border(2.dp, Color.Black), // Border for the main menu
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Menu Options
                menuOptions.forEachIndexed { index, option ->
                    MenuOption(
                        text = option.first,
                        isSelected = index == selectedIndex,
                        onClick = {
                            vibrate(context)
                            selectedIndex = index
                            option.second.let { buttonType ->
                                selectedButtonType = buttonType
                                viewModel.updateButtonType(buttonType)
                                Toast.makeText(
                                    context,
                                    "${menuOptions[selectedIndex].first} ${context.getString(R.string.is_selected)}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
                }
            }

            Text(
                text = stringResource(id = R.string.done),
                fontFamily = FontFamily(Font(R.font.nokia_font)),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = LightGreen,
                modifier = Modifier
                    .clickable {
                        if (ClickDebouncer.canClick()) {
                            viewModel.updateJoyStickSize(joyStickSize)
                            vibrate(context)
                            navController.popBackStack()
                        }
                    }
                    .background(Color.Black)
                    .padding(horizontal = 32.dp, vertical = 8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}