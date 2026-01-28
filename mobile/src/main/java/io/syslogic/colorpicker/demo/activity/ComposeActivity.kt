package io.syslogic.colorpicker.demo.activity

import android.content.SharedPreferences
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.preference.PreferenceManager

import io.syslogic.colorpicker.compose.OnColorChangedListener
import io.syslogic.colorpicker.compose.ColorPickerComponent
import androidx.core.content.edit

/**
 * Jetpack Compose {@link ComponentActivity}
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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ColorPickerComponent(
                        initialColor = Color(initialColor),
                        onColorChanged = this@ComposeActivity,
                        showAlpha = true,
                        showHSV = true,
                        showARGB = true,
                        showHex = true
                    )
                }
            }
        }
    }

    /** It overrides interface {@link OnColorChangedListener}. */
    override fun onColorChanged(color: Int) {
        this.prefs.edit { putInt("color_code_01", color) }
        println("onColorChanged: $color")
    }
}
