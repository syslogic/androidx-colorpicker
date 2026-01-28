package io.syslogic.colorpicker.demo

import android.content.res.Resources.getSystem
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.click
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performMouseInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpRect
import androidx.test.ext.junit.runners.AndroidJUnit4

import io.syslogic.colorpicker.compose.ColorPickerComponent
import io.syslogic.colorpicker.compose.OnColorChangedListener

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import java.util.Random

import kotlin.math.roundToInt

/**
 * Compose Content Test Case
 * @author Martin Zeitler
 */
@RunWith(AndroidJUnit4::class)
class TestComposeContent : TestSuite() {

    @get:Rule
    var testRule: ComposeContentTestRule = createComposeRule()

    @Before
    fun setUp() {
        testRule.setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ColorPickerComponent(
                        initialColor = Color(0xFF444444.toInt()),
                        onColorChanged = {} as OnColorChangedListener?,
                        showAlpha = true,
                        showHSV = true,
                        showARGB = true,
                        showHex = true
                    )
                }
            }
        }
        // testRule.onRoot().printToLog(logTag)
        // testRule.onRoot().getBoundsInRoot()
    }

    /** Randomly clicking the sat/val panel. */
    @Test
    @OptIn(ExperimentalTestApi::class)
    fun satValPanelTest() {
        randomlyClick(getNodeWithTag("sat_val"))
    }

    /** Randomly clicking the hue panel. */
    @Test
    @OptIn(ExperimentalTestApi::class)
    fun huePanelTest() {
        randomlyClick(getNodeWithTag("hue"))
    }

    /** Randomly clicking the alpha panel. */
    @Test
    @OptIn(ExperimentalTestApi::class)
    fun alphaPanelTest() {
        randomlyClick(getNodeWithTag("alpha"))
    }

    /** Testing if the elements are there. */
    @Test
    fun basicTest() {
        getNodeWithTag("sat_val").assertExists().assertIsDisplayed()
        getNodeWithTag("hue").assertExists().assertIsDisplayed()
        getNodeWithTag("alpha").assertExists().assertIsDisplayed()
        getNodeWithTag("hex").assertExists().assertIsDisplayed()
        getNodeWithTag("old_color").assertExists().assertIsDisplayed()
        getNodeWithTag("new_color").assertExists().assertIsDisplayed()
        getNodeWithTag("value_hue").assertExists().assertIsDisplayed()
        getNodeWithTag("value_sat").assertExists().assertIsDisplayed()
        getNodeWithTag("value_val").assertExists().assertIsDisplayed()
        getNodeWithTag("value_alpha").assertExists().assertIsDisplayed()
        getNodeWithTag("value_blue").assertExists().assertIsDisplayed()
        getNodeWithTag("value_red").assertExists().assertIsDisplayed()
        getNodeWithTag("value_green").assertExists().assertIsDisplayed()
    }

    /** Convert dp to px. */
    private val Dp.px: Int get() = (this.value * getSystem().displayMetrics.density).roundToInt()

    /** Getter for interaction nodes. */
    private fun getNodeWithTag(testTag: String): SemanticsNodeInteraction {
        return testRule.onNodeWithTag(testTag = testTag)
    }

    /** Randomly click. */
    @ExperimentalTestApi
    private fun randomlyClick(interaction: SemanticsNodeInteraction, count: Int = 250) {
        for (i in 0 until count) {
            interaction.performMouseInput { click(getOffset(interaction)) }
            sleep(20)
        }
    }

    /** Random offset. */
    private fun getOffset(interaction: SemanticsNodeInteraction): Offset {
        val rect: DpRect = interaction.getBoundsInRoot()
        val boundX = rect.right.px - rect.left.px
        val boundY = rect.bottom.px - rect.top.px
        val x: Float = (Random().nextInt(boundX + 1)).toFloat() /* left */
        val y: Float = (Random().nextInt(boundY + 1)).toFloat() /* top */
        return Offset(x, y)
    }
}
