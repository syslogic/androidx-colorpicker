package io.syslogic.demo.colorpicker.activity

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import io.syslogic.colorpicker.OnColorChangedListener

import io.syslogic.colorpicker.compose.ColorPickerComponent

/**
 * Jetpack Compose Activity
 *
 * @author Martin Zeitler
 */
class ComposeActivity : ComponentActivity(), OnColorChangedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContent {
            MaterialTheme {
                ColorPickerComponent(
                    initialColor = Color.GREEN,
                    onColorChanged = this@ComposeActivity,
                    showAlphaSlider = true,
                    showHexValue = true
                )
            }
        }
    }

    override fun onColorChanged(color: Int) {
        println("current color: $color")
    }
}
