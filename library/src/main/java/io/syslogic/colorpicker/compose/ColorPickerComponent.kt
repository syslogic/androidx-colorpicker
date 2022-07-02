package io.syslogic.colorpicker.compose

import android.content.Context
import android.graphics.RectF
import android.widget.Toast

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize

import io.syslogic.colorpicker.OnColorChangedListener
import io.syslogic.colorpicker.R
import io.syslogic.colorpicker.compose.LayoutId.*

import java.util.*

/**
 * Jetpack Compose Color-Picker Component
 *
 * @author Martin Zeitler
 */
@Composable
fun ColorPickerComponent(
    initialColor: Color = Color.Transparent,
    onColorChanged: OnColorChangedListener?,
    showAlpha: Boolean = true,
    showARGB: Boolean = true,
    showHSV: Boolean = true,
    showHex: Boolean = true
) {

    val context = LocalContext.current
    val rowPadding = dimensionResource(R.dimen.compose_row_padding)
    val listener: OnColorChangedListener? = onColorChanged

    var currentAlpha: Float by remember { mutableStateOf(.5F) }
    var currentSat: Float by remember { mutableStateOf(0.0F) }
    var currentVal: Float by remember { mutableStateOf(0.0F) }
    var currentHue: Float by remember { mutableStateOf(180F) }
    var currentColor: Int by remember { mutableStateOf(initialColor.hashCode()) }

    var offsetSatVal by remember { mutableStateOf(Offset.Zero) }
    var sizeSatVal by remember { mutableStateOf(IntSize.Zero) }

    var offsetHue by remember { mutableStateOf(Offset.Zero) }
    var sizeHue by remember { mutableStateOf(IntSize.Zero) }

    var offsetAlpha by remember { mutableStateOf(Offset.Zero) }
    var sizeAlpha by remember { mutableStateOf(IntSize.Zero) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {

        /* HSV & ARGB */
        if (showHSV || showARGB) {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.padding(all = rowPadding)
            ) {

                if (showHSV) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .padding(all = rowPadding)
                            .weight(0.5F)
                    ) {
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(all = rowPadding)
                        ) {
                            Text(
                                text = "Hue:",
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .defaultMinSize(minWidth = Dp(42F))
                                    .testTag("text_hue")
                            )
                            Text(
                                modifier = Modifier.testTag("value_hue"),
                                text = "$currentHue"
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(all = rowPadding)
                        ) {
                            Text(
                                text = "Sat:",
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .defaultMinSize(minWidth = Dp(42F))
                                    .testTag("text_sat")
                            )
                            Text(
                                modifier = Modifier.testTag("value_sat"),
                                text = "$currentSat"
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(all = rowPadding)
                        ) {
                            Text(
                                text = "Val:",
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .defaultMinSize(minWidth = Dp(42F))
                                    .testTag("text_val")
                            )
                            Text(
                                modifier = Modifier.testTag("value_val"),
                                text = "$currentVal"
                            )
                        }
                    }
                }

                if (showARGB) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .padding(all = rowPadding)
                            .weight(0.5F)
                    ) {
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(all = rowPadding)
                        ) {
                            Text(
                                text = "Alpha:",
                                modifier = Modifier.testTag("text_alpha")
                            )
                            Text(
                                text = getAlphaChannel(currentColor),
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .defaultMinSize(minWidth = Dp(30F))
                                    .testTag("value_alpha")
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(all = rowPadding)
                        ) {
                            Text(
                                text = "Blue:",
                                modifier = Modifier.testTag("text_blue")
                            )
                            Text(
                                text = getBlueChannel(currentColor),
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .defaultMinSize(minWidth = Dp(30F))
                                    .testTag("value_blue")
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(all = rowPadding)
                        ) {
                            Text(
                                text = "Red:",
                                modifier = Modifier.testTag("text_red")
                            )
                            Text(
                                text = getRedChannel(currentColor),
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .defaultMinSize(minWidth = Dp(30F))
                                    .testTag("value_red")
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(all = rowPadding)
                        ) {
                            Text(
                                text = "Green:",
                                modifier = Modifier.testTag("text_green")
                            )
                            Text(
                                text = getGreenChannel(currentColor),
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .defaultMinSize(minWidth = Dp(30F))
                                    .testTag("value_green")
                            )
                        }
                    }
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(all = rowPadding)
        ) {

            /* Sat/Val Panel */
            Image(
                contentDescription = "Sat/Val by Hue",
                contentScale = ContentScale.FillBounds,
                painter = SatValPainter(Size(900F, 900F)).also {
                    it.setValue(currentSat, currentVal)
                    it.setHue(currentHue)
                },
                modifier = Modifier
                    .layoutId(SatVal)
                    .testTag("sat_val")
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = { event ->
                                val rect: RectF = getLayoutBounds(offsetSatVal, sizeSatVal)
                                currentSat = pointToSat(rect, event.x)
                                currentVal = pointToVal(rect, event.y)
                                currentColor = toIntColor(currentAlpha, currentHue, currentSat, currentVal, listener)
                                println("Sat: $currentSat, Val: $currentVal (${event.x.toInt()} x ${event.y.toInt()})")
                            }
                        )
                    }
                    .onGloballyPositioned { coordinates ->
                        offsetSatVal = coordinates.positionInRoot()
                        sizeSatVal = coordinates.size
                    }
            )

            Spacer(
                modifier = Modifier.padding(all = rowPadding)
            )

            /* Hue Panel */
            Image(
                contentDescription = "Hue",
                contentScale = ContentScale.FillBounds,
                painter = HuePainter(Size(100F, 900F)).also {
                    it.setValue(currentHue)
                },
                modifier = Modifier
                    .layoutId(Hue)
                    .testTag("hue")
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = { event ->
                                val rect: RectF = getLayoutBounds(offsetHue, sizeHue)
                                currentHue = pointToHue(rect, event.y)
                                currentColor = toIntColor(currentAlpha, currentHue, currentSat, currentVal, listener)
                                println("Val: $currentHue (${event.y.toInt()})")
                            }
                        )
                    }
                    .onGloballyPositioned { coordinates ->
                        offsetHue = coordinates.positionInRoot()
                        sizeHue = coordinates.size
                    }
            )
        }

        /* Alpha Panel */
        if (showAlpha) {
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
                            it.setAlphaByColor(currentColor)
                        },
                        modifier = Modifier
                            .layoutId(Alpha)
                            .testTag("alpha")
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = { event ->
                                        val rect: RectF = getLayoutBounds(offsetAlpha, sizeAlpha)
                                        currentAlpha = pointToAlpha(rect, event.x)
                                        currentColor = toIntColor(currentAlpha, currentHue, currentSat, currentVal, listener)
                                        println("Alpha: $currentAlpha (${event.x.toInt()})")
                                    }
                                )
                            }
                            .onGloballyPositioned { coordinates ->
                                offsetAlpha = coordinates.positionInRoot()
                                sizeAlpha = coordinates.size
                            }
                    )
                }
            }
        }

        /* Hex Value */
        if (showHex) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(all = rowPadding)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Hex Value: ${toARGB(currentColor)}",
                        modifier = Modifier.testTag("hex")
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
                    contentDescription = "Old Color",
                    contentScale = ContentScale.FillBounds,
                    painter = ColorPainter(Size(400F, 120F)).also {
                        it.setValue(initialColor)
                    },
                    modifier = Modifier
                        .layoutId(OldColor)
                        .testTag("old_color")
                        .padding(all = rowPadding)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    onButtonClick(context, OldColor, initialColor.value.toInt())
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
                    contentDescription = "New Color",
                    contentScale = ContentScale.FillBounds,
                    painter = ColorPainter(Size(400F, 120F)).also {
                        it.setValue(currentColor)
                    },
                    modifier = Modifier
                        .layoutId(NewColor)
                        .testTag("new_color")
                        .padding(all = rowPadding)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    onButtonClick(
                                        context,
                                        NewColor,
                                        toIntColor(currentAlpha, currentHue, currentSat, currentVal, listener)
                                    )
                                }
                            )
                        }
                )
            }
        }
    }
}

fun onButtonClick(context: Context, layoutId: LayoutId, value: Int) {
    val message: String = when (layoutId) {
        OldColor -> { "OldColor:\n${toARGB(value)}" }
        NewColor -> { "NewColor:\n${toARGB(value)}" }
        else -> { return }
    }
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    println(message)
}

/* left, top, right, bottom */
fun getLayoutBounds(position: Offset, size: IntSize): RectF {
    return RectF(
        /* left */ position.x,
        /* top */ position.y,
        position.x + size.width.toFloat(),
        position.y + size.height.toFloat()
    )
}

fun toIntColor(alpha: Float, hue: Float, sat: Float, value: Float, listener: OnColorChangedListener?) : Int {
    println("alpha: $alpha, hue: $hue, saturation: $sat, contrast: $value")
    val colorCode: Int = android.graphics.Color.HSVToColor(
        (alpha * 255).toInt(), floatArrayOf(hue, sat, value)
    )
    listener?.onColorChanged(colorCode)
    return colorCode
}

/**
 * @param color the color value to convert.
 */
fun toARGB(color: Int): String {
    var alpha = Integer.toHexString(Color(color).alpha.times(255).toInt()).uppercase(Locale.ROOT)
    var red = Integer.toHexString(Color(color).red.times(255).toInt()).uppercase(Locale.ROOT)
    var green = Integer.toHexString(Color(color).green.times(255).toInt()).uppercase(Locale.ROOT)
    var blue = Integer.toHexString(Color(color).blue.times(255).toInt()).uppercase(Locale.ROOT)
    if (alpha.length == 1) {alpha = String.format("0%s", alpha)}
    if (red.length   == 1) {red   = String.format("0%s", red)}
    if (green.length == 1) {green = String.format("0%s", green)}
    if (blue.length  == 1) {blue  = String.format("0%s", blue)}
    return String.format("#%s%s%s%s", alpha, red, green, blue)
}

/**
 * @param color the color value to convert.
 * @return channel integer value as String.
 */
fun getAlphaChannel(color: Int): String {
    return String.format("%s", Color(color).alpha.times(255).toInt())
}

/**
 * @param color the color value to convert.
 * @return channel integer value as String.
 */
fun getRedChannel(color: Int): String {
    return String.format("%s", Color(color).red.times(255).toInt())
}

/**
 * @param color the color value to convert.
 * @return channel integer value as String.
 */
fun getGreenChannel(color: Int): String {
    return String.format("%s", Color(color).green.times(255).toInt())
}

/**
 * @param color the color value to convert.
 * @return channel integer value as String.
 */
fun getBlueChannel(color: Int): String {
    return String.format("%s", Color(color).blue.times(255).toInt())
}

/**
 * @param rect boundaries of the hue rectangle.
 * @param y the value on the y axis.
 * @return float hue.
 */
private fun pointToHue(rect: RectF, y: Float): Float {
    return 360f - y * 360f / rect.height()
}

/**
 * @param rect boundaries of the sat/val rectangle.
 * @param x the value on the x axis.
 * @return float sat.
 */
private fun pointToSat(rect: RectF, x: Float): Float {
    return 1f / rect.width() * x
}

/**
 * @param rect boundaries of the sat/val rectangle.
 * @param y the value on the x axis.
 * @return float value.
 */
private fun pointToVal(rect: RectF, y: Float): Float {
    return 1f - 1f / rect.height() * y
}

/**
 * @param rect boundaries of the alpha rectangle.
 * @param x the value on the x axis.
 * @return float alpha.
 */
private fun pointToAlpha(rect: RectF, x: Float): Float {
    return x / rect.width()
}
