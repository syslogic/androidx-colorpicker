package io.syslogic.colorpicker.compose

import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp

import io.syslogic.colorpicker.OnColorChangedListener
import io.syslogic.colorpicker.R

import java.util.*

@Composable
fun ColorPickerComponent(
    initialColor: Int = Color.BLACK,
    onColorChanged: OnColorChangedListener?,
    showAlphaSlider: Boolean = false,
    showHexValue: Boolean = true
) {

    @Suppress("CanBeVal")
    var currentColor: Int = initialColor
    var listener: OnColorChangedListener? = onColorChanged

    val rowPadding = dimensionResource(R.dimen.compose_row_padding)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(Dp(1000F))
    ) {

        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(all = rowPadding)
        ) {

            /* Hue */
            Image(
                painter = HuePainter(Size(900F, 900F)),
                contentDescription = "",
                contentScale = ContentScale.FillBounds
            )

            Box(
                modifier = Modifier.padding(all = rowPadding)
            )

            /* Sat */
            Image(
                painter = SatPainter(Size(100F, 900F)),
                contentDescription = "",
                contentScale = ContentScale.FillBounds
            )
        }

        /* Alpha */
        if (showAlphaSlider) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(all = rowPadding)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(fraction = 1.0f),
                    contentAlignment = Alignment.Center
                ) {

                    Image(
                        painter = AlphaPainter(Size(904F, 80F)),
                        contentDescription = "",
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }

        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(all = rowPadding)
        ) {

            /* Old Color */
            Box(
                modifier = Modifier.weight(.46F),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = ColorPainter(Size(800F, 120F))
                        .also { it.setColor(initialColor) },
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds
                )
            }

            Image(
                painter = painterResource(id = R.drawable.ic_baseline_keyboard_double_arrow_right_24),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.weight(.08F)
            )

            /* New Color */
            Box(
                modifier = Modifier.weight(.46F),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = ColorPainter(Size(800F, 120F))
                        .also { it.setColor(initialColor) },
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds
                )
            }
        }

        /* Hex Value */
        if (showHexValue) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(all = rowPadding)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    val value = convertToARGB(currentColor)
                    Text(
                        text = "Hex Value: $value"
                    )
                }
            }
        }
    }
}

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

/**
 * @param color the color value to convert.
 */
fun convertToARGB(color: Int): String {
    var alpha = Integer.toHexString(Color.alpha(color)).uppercase(Locale.getDefault())
    var red = Integer.toHexString(Color.red(color)).uppercase(Locale.getDefault())
    var green = Integer.toHexString(Color.green(color)).uppercase(Locale.getDefault())
    var blue = Integer.toHexString(Color.blue(color)).uppercase(Locale.getDefault())
    if (alpha.length == 1) {alpha = String.format("0%s", alpha)}
    if (red.length == 1) {red = String.format("0%s", red)}
    if (green.length == 1) {green = String.format("0%s", green)}
    if (blue.length == 1) {blue = String.format("0%s", blue)}
    return String.format("#%s%s%s%s", alpha, red, green, blue)
}
