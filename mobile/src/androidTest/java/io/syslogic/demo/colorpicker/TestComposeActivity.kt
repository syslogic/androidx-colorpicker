package io.syslogic.demo.colorpicker

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.click
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performMouseInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.unit.DpRect
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4

import io.syslogic.demo.colorpicker.activity.ComposeActivity

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Random

/**
 * Compose Activity Test Case
 *
 * @author Martin Zeitler
 */
@RunWith(AndroidJUnit4::class)
class TestComposeActivity : TestSuite() {

    @get:Rule
    val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComposeActivity>, ComposeActivity> =
        createAndroidComposeRule(ComposeActivity::class.java)

    lateinit var interaction: SemanticsNodeInteraction

    @Test
    fun logTest() {
        interaction = composeTestRule.onRoot()
        interaction.printToLog("TAG")
    }

    @Test
    @OptIn(ExperimentalTestApi::class)
    fun huePanelTest() {

        interaction = getNodeWithTag("hue")
        interaction.assertExists().assertIsDisplayed()

        /* Randomly tapping the huePanel for 5 seconds. */
        for (i in 0..99) {
            randomlyClick(interaction)
            sleep(50)
        }
    }

    @Test
    @OptIn(ExperimentalTestApi::class)
    fun satValPanelTest() {

        interaction = getNodeWithTag("sat_val")
        interaction.assertExists().assertIsDisplayed()

        /* Randomly tapping the satValPanel for 5 seconds. */
        for (i in 0..99) {
            randomlyClick(interaction)
            sleep(50)
        }
    }

    @Test
    @OptIn(ExperimentalTestApi::class)
    fun alphaPanelTest() {

        interaction = getNodeWithTag("alpha")
        interaction.assertExists().assertIsDisplayed()

        /* Randomly tapping the alphaPanel for 5 seconds. */
        for (i in 0..99) {
            randomlyClick(interaction)
            sleep(50)
        }
    }

    @Test
    fun hexColorTest() {
        interaction = getNodeWithTag("hex")
        interaction.assertExists().assertIsDisplayed()
    }

    @Test
    fun oldColorTest() {
        interaction = getNodeWithTag("old_color")
        interaction.assertExists().assertIsDisplayed()
    }

    @Test
    fun newColorTest() {
        interaction = getNodeWithTag("new_color")
        interaction.assertExists().assertIsDisplayed()
    }

    @Test
    fun valueHueTest() {
        interaction = getNodeWithTag("value_hue")
        interaction.assertExists().assertIsDisplayed()
    }

    @Test
    fun valueSatTest() {
        interaction = getNodeWithTag("value_sat")
        interaction.assertExists().assertIsDisplayed()
    }

    @Test
    fun valueValTest() {
        interaction = getNodeWithTag("value_val")
        interaction.assertExists().assertIsDisplayed()
    }

    @Test
    fun valueAlphaTest() {
        interaction = getNodeWithTag("value_alpha")
        interaction.assertExists().assertIsDisplayed()
    }

    @Test
    fun valueBlueTest() {
        interaction = getNodeWithTag("value_blue")
        interaction.assertExists().assertIsDisplayed()
    }

    @Test
    fun valueRedTest() {
        interaction = getNodeWithTag("value_red")
        interaction.assertExists().assertIsDisplayed()
    }

    @Test
    fun valueGreenTest() {
        interaction = getNodeWithTag("value_green")
        interaction.assertExists().assertIsDisplayed()
    }

    @Override
    @ExperimentalTestApi
    private fun randomlyClick(interaction: SemanticsNodeInteraction) {
        val rect: DpRect = interaction.getBoundsInRoot()
        val rnd = Random()
        val coordinate = floatArrayOf(
            ((rect.left.value + rnd.nextInt((rect.right.value - rect.left.value + 1).toInt()))),
            ((rect.top.value + rnd.nextInt((rect.bottom.value - rect.top.value + 1).toInt())))
        )
        val position = Offset(coordinate[0], coordinate[1])
        interaction.performMouseInput {
            click(position)
        }
    }

    private fun getNodeWithTag(testTag: String): SemanticsNodeInteraction {
        return composeTestRule.onNodeWithTag(testTag = testTag)
    }
}
