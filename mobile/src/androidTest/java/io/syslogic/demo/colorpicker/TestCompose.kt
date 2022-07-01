package io.syslogic.demo.colorpicker

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import io.syslogic.colorpicker.compose.ColorPickerComponent
import org.junit.Rule
import org.junit.Test

class TestCompose {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun myTest() {
        composeTestRule.setContent {
            MaterialTheme {
                ColorPickerComponent(
                    initialColor = Color.DarkGray.hashCode(),
                    showAlphaSlider = true,
                    showHexValue = true,
                    onColorChanged = null
                )
            }
        }
        composeTestRule.onNodeWithTag(
            testTag = "Welcome",
            useUnmergedTree = false
        ).assertIsDisplayed()
    }
}
