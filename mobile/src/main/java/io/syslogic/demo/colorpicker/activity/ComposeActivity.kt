package io.syslogic.demo.colorpicker.activity

import android.content.SharedPreferences

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.preference.PreferenceManager

import io.syslogic.colorpicker.OnColorChangedListener

import io.syslogic.colorpicker.compose.ColorPickerComponent

/**
 * Jetpack Compose {@link ComponentActivity}
 *
 * @author Martin Zeitler
 */
class ComposeActivity : ComponentActivity(), OnColorChangedListener {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val initialColor = prefs.getInt("color_code_01", 0xFF444444.toInt())

        this.setContent {
            MaterialTheme {
                ColorPickerComponent(
                    onColorChanged = this@ComposeActivity,
                    initialColor = Color(initialColor),
                    showAlpha = true,
                    showHSV = true,
                    showARGB = true,
                    showHex = true
                )
            }
        }
    }

    /** It overrides interface {@link OnColorChangedListener}. */
    override fun onColorChanged(color: Int) {
        this.prefs.edit().putInt("color_code_01", color).apply()
        println("onColorChanged: $color")
    }
}
