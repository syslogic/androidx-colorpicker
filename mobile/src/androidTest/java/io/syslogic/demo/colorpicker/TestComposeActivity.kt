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
        composeTestRule.onNodeWithTag(testTag = "sat").assertIsDisplayed()
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
        composeTestRule.onNodeWithTag(testTag = "old").assertIsDisplayed()
    }

    @Test
    fun newColorTest() {
        composeTestRule.onNodeWithTag(testTag = "new").assertIsDisplayed()
    }
}
