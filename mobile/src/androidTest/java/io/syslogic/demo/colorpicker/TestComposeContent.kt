package io.syslogic.demo.colorpicker

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
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performMouseInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpRect
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.syslogic.colorpicker.compose.ColorPickerComponent
import org.junit.Before

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Random

/**
 * Compose Content Test Case
 * @author Martin Zeitler
 */
@RunWith(AndroidJUnit4::class)
class TestComposeContent : TestSuite() {

    @get:Rule
    var testRule: ComposeContentTestRule = createComposeRule()

    private val logTag: String = TestComposeContent::class.java.simpleName

    private lateinit var interaction: SemanticsNodeInteraction

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
                        onColorChanged = {},
                        showAlpha = true,
                        showHSV = true,
                        showARGB = true,
                        showHex = true
                    )
                }
            }
        }
    }

    @Test
    fun printToLogTest() {
        interaction = testRule.onRoot()
        interaction.printToLog(logTag)
    }

    /** Randomly tapping the huePanel. */
    @Test
    @OptIn(ExperimentalTestApi::class)
    fun huePanelTest() {
        interaction = getNodeWithTag("hue")
        interaction.assertExists().assertIsDisplayed()
        randomlyClick(interaction)
    }

    /** Randomly tapping the satValPanel . */
    @Test
    @OptIn(ExperimentalTestApi::class)
    fun satValPanelTest() {
        interaction = getNodeWithTag("sat_val")
        interaction.assertExists().assertIsDisplayed()
        randomlyClick(interaction)
    }

    /** Randomly tapping the alphaPanel. */
    @Test
    @OptIn(ExperimentalTestApi::class)
    fun alphaPanelTest() {
        interaction = getNodeWithTag("alpha")
        interaction.assertExists().assertIsDisplayed()
        randomlyClick(interaction)
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

    private fun getNodeWithTag(testTag: String): SemanticsNodeInteraction {
        return testRule.onNodeWithTag(testTag = testTag)
    }

    private val Dp.px: Int get() = (this.value / getSystem().displayMetrics.density).toInt()

    /* Randomly tapping. */
    @ExperimentalTestApi
    private fun randomlyClick(interaction: SemanticsNodeInteraction, count: Int = 100, ms: Int = 50) {
        val rnd = Random()
        val rect: DpRect = interaction.getBoundsInRoot()
        println("DpRect >> x: " + rect.left.px + "px, y: " + rect.top.px + "px")
        for (i in 0 until count) {
            val coordinate = floatArrayOf(
                (rect.left.px + rnd.nextInt(rect.right.px - rect.left.px + 1)).toFloat(),
                (rect.top.px + rnd.nextInt(rect.bottom.px - rect.top.px + 1)).toFloat()
            )
            val position = Offset(coordinate[0], coordinate[1])
            println("Offset >> x: " + position.x.toInt() + ", y: " + position.y.toInt())
            interaction.performMouseInput {
                click(position)
            }
            sleep(ms)
        }
    }
}
