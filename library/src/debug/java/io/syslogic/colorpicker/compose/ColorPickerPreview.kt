package io.syslogic.colorpicker.compose

import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview(
    name = "PIXEL_4_XL",
    device = Devices.PIXEL_4_XL,
    showBackground = true,
    showSystemUi = true
)
fun ColorPickerPreview() {
    ColorPickerComponent(
        initialColor = Color.DKGRAY,
        showAlphaSlider = true,
        showHexValue = true,
        onColorChanged = null
    )
}
