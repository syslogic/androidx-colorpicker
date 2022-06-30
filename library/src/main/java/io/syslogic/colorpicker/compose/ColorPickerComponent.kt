package io.syslogic.colorpicker.compose

import android.content.res.Configuration.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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
    initialColor: Int = Color.Transparent.hashCode(),
    onColorChanged: OnColorChangedListener?,
    showAlphaSlider: Boolean = false,
    showHexValue: Boolean = true
) {

    @Suppress("CanBeVal")
    var currentColor: Int = initialColor
    @Suppress("UNUSED_VARIABLE")
    var listener: OnColorChangedListener? = onColorChanged
    val rowPadding = dimensionResource(R.dimen.compose_row_padding)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(Dp(1000F))
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(all = rowPadding)
        ) {

            /* Hue Panel */
            Image(
                contentDescription = "Hue",
                contentScale = ContentScale.FillBounds,
                painter = HuePainter(Size(900F, 900F)).also {
                    it.setColor(initialColor)
                },
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            println("Hue.onPress ${it.x} x ${it.y}")
                        }
                    )
            })

            Spacer(
                modifier = Modifier.padding(all = rowPadding)
            )

            /* Saturation Panel */
            Image(
                contentDescription = "Saturation",
                contentScale = ContentScale.FillBounds,
                painter = SatPainter(Size(100F, 900F)).also {
                    it.setColor(initialColor)
                },
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            println("Saturation.onPress ${it.x} x ${it.y}")
                        }
                    )
                }
            )
        }

        /* Alpha Slider */
        if (showAlphaSlider) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(all = rowPadding)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        contentDescription = "Alpha Slider",
                        contentScale = ContentScale.FillBounds,
                        painter = AlphaPainter(Size(1030F, 80F)).also {
                            it.setAlpha(initialColor)
                        },
                        modifier = Modifier.pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    println("AlphaSlider.onPress ${it.x} x ${it.y}")
                                }
                            )
                        }
                    )
                }
            }
        }

        /* Hex Value */
        if (showHexValue) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(all = rowPadding)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Hex Value: ${convertToARGB(currentColor)}"
                    )
                }
            }
        }

        /* Color Selector */
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(all = rowPadding)
        ) {

            Box(
                modifier = Modifier.weight(.46F),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    contentDescription = "Old Color Panel",
                    contentScale = ContentScale.FillBounds,
                    painter = ColorPainter(Size(800F, 120F)).also {
                        it.setColor(initialColor)
                    },
                    modifier = Modifier
                        .padding(all = rowPadding)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    println("OldColor.onPress")
                                }
                            )
                        }
                )
            }

            Image(
                contentDescription = "Arrow Right",
                contentScale = ContentScale.FillBounds,
                painter = painterResource(id = R.drawable.ic_baseline_keyboard_double_arrow_right_24),
                modifier = Modifier.weight(.08F)
            )

            Box(
                modifier = Modifier.weight(.46F),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    contentDescription = "New Color Panel",
                    contentScale = ContentScale.FillBounds,
                    painter = ColorPainter(Size(800F, 120F)).also {
                        it.setColor(initialColor)
                    },
                    modifier = Modifier
                        .padding(all = rowPadding)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    println("NewColor.onPress")
                                }
                            )
                        }
                )
            }
        }
    }
}

@Composable
@Preview(
    name = "PIXEL_4_XL",
    device = Devices.PIXEL_4_XL,
    uiMode = UI_MODE_NIGHT_YES,
    showBackground = true,
    showSystemUi = true
)
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

/**
 * @param color the color value to convert.
 */
fun convertToARGB(color: Int): String {
    var alpha = Integer.toHexString(Color(color).alpha.toInt()).uppercase(Locale.getDefault())
    var red = Integer.toHexString(Color(color).red.toInt()).uppercase(Locale.getDefault())
    var green = Integer.toHexString(Color(color).green.toInt()).uppercase(Locale.getDefault())
    var blue = Integer.toHexString(Color(color).blue.toInt()).uppercase(Locale.getDefault())
    if (alpha.length == 1) {alpha = String.format("0%s", alpha)}
    if (red.length   == 1) {red   = String.format("0%s", red)}
    if (green.length == 1) {green = String.format("0%s", green)}
    if (blue.length  == 1) {blue  = String.format("0%s", blue)}
    return String.format("#%s%s%s%s", alpha, red, green, blue)
}
