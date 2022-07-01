package io.syslogic.demo.colorpicker

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog

import io.syslogic.demo.colorpicker.activity.ComposeActivity

import org.junit.Rule
import org.junit.Test

/**
 * Color-Picker Test Case
 *
 * @author Martin Zeitler
 */
class TestComposeActivity {

    @get:Rule
    val composeTestRule = createAndroidComposeRule(ComposeActivity::class.java)

    @Test
    fun logTest() {
        composeTestRule.onRoot().printToLog("TAG")
    }

    @Test
    fun huePanelTest() {
        composeTestRule.onNodeWithTag(testTag = "hue").assertIsDisplayed()
    }

    @Test
    fun satPanelTest() {
        composeTestRule.onNodeWithTag(testTag = "sat_val").assertIsDisplayed()
    }

    @Test
    fun alphaPanelTest() {
        composeTestRule.onNodeWithTag(testTag = "alpha").assertIsDisplayed()
    }

    @Test
    fun hexColorTest() {
        composeTestRule.onNodeWithTag(testTag = "hex").assertIsDisplayed()
    }

    @Test
    fun oldColorTest() {
        composeTestRule.onNodeWithTag(testTag = "old_color").assertIsDisplayed()
    }

    @Test
    fun newColorTest() {
        composeTestRule.onNodeWithTag(testTag = "new_color").assertIsDisplayed()
    }

    @Test
    fun valueHueTest() {
        composeTestRule.onNodeWithTag(testTag = "value_hue").assertIsDisplayed()
    }

    @Test
    fun valueSatTest() {
        composeTestRule.onNodeWithTag(testTag = "value_sat").assertIsDisplayed()
    }

    @Test
    fun valueValTest() {
        composeTestRule.onNodeWithTag(testTag = "value_val").assertIsDisplayed()
    }

    @Test
    fun valueAlphaTest() {
        composeTestRule.onNodeWithTag(testTag = "value_alpha").assertIsDisplayed()
    }

    @Test
    fun valueBlueTest() {
        composeTestRule.onNodeWithTag(testTag = "value_blue").assertIsDisplayed()
    }

    @Test
    fun valueRedTest() {
        composeTestRule.onNodeWithTag(testTag = "value_red").assertIsDisplayed()
    }

    @Test
    fun valueGreenTest() {
        composeTestRule.onNodeWithTag(testTag = "value_green").assertIsDisplayed()
    }
}
