package io.syslogic.colorpicker.compose

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
    name = "ColorPicker",
    showBackground = true
)
@Composable
fun ColorPickerPreview() {
    MaterialTheme {
        ColorPickerComponent(
            initialColor = Color(0xD31A3D9A),
            onColorChanged = null,
            showAlpha = true,
            showARGB = true,
            showHSV = true,
            showHex = true
        )
    }
}
