package io.syslogic.demo.colorpicker.activity

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.preference.PreferenceManager
import io.syslogic.colorpicker.OnColorChangedListener

import io.syslogic.colorpicker.compose.ColorPickerComponent

/**
 * Jetpack Compose Activity
 *
 * @author Martin Zeitler
 */
class ComposeActivity : ComponentActivity(), OnColorChangedListener {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this)
        this.setContent {
            MaterialTheme {
                ColorPickerComponent(
                    initialColor = prefs.getInt("color_code_01", Color.BLACK),
                    onColorChanged = this@ComposeActivity,
                    showAlphaSlider = true,
                    showHexValue = true
                )
            }
        }
    }

    override fun onColorChanged(color: Int) {
        this.prefs.edit().putInt("color_code_01", color).apply()
        println("onColorChanged: $color")
    }
}
