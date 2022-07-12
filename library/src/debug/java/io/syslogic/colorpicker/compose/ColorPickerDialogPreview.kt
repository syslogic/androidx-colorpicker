package io.syslogic.colorpicker.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

/**
 * ColorPickerDialog Preview
 *
 * @author Martin Zeitler
 */
@Preview(
    name = "ColorPickerDialog",
    showBackground = true
)
@Composable
fun ColorPickerDialogPreview() {
    MaterialTheme {
        ColorPickerDialog(
            initialColor = Color(0xD31A3D9A),
            onColorChanged = null,
            showAlpha = true,
            showARGB = false,
            showHSV = false,
            showHex = true,
            showDialog = {

            }
        )
    }
}
