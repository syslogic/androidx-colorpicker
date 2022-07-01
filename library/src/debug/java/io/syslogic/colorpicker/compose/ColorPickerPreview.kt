package io.syslogic.colorpicker.compose

import android.content.res.Configuration
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ColorPickerPreview() {
    MaterialTheme {
        ColorPickerComponent(
            initialColor = Color.DarkGray.hashCode(),
            showAlphaSlider = true,
            showHexValue = true,
            onColorChanged = null
        )
    }
}
