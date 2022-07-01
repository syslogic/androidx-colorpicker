package io.syslogic.colorpicker.compose

import android.content.res.Configuration
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

/**
 * ColorPicker Preview
 *
 * @author Martin Zeitler
 */
@Preview(
    group = "day_night",
    name = "ColorPicker NIGHT_NO",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Preview(
    group = "day_night",
    name = "ColorPicker NIGHT_YES",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun ColorPickerPreview() {
    MaterialTheme {
        ColorPickerComponent(
            initialColor = Color.DarkGray,
            onColorChanged = null,
            showAlpha = true,
            showHex = true,
            showHSV = true
        )
    }
}
