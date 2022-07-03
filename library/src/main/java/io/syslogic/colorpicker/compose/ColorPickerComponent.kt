package io.syslogic.colorpicker.compose

import android.content.Context
import android.graphics.Color.HSVToColor
import android.graphics.Color.RGBToHSV
import android.graphics.RectF
import android.widget.Toast

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize

import io.syslogic.colorpicker.OnColorChangedListener
import io.syslogic.colorpicker.R
import io.syslogic.colorpicker.compose.LayoutId.*

/**
 * Jetpack Compose Color-Picker Component
 *
 * @author Martin Zeitler
 */
@Composable
fun ColorPickerComponent(
    initialColor: Color = Color.Unspecified,
    onColorChanged: OnColorChangedListener?,
    showAlpha: Boolean = true,
    showHSV: Boolean = true,
    showARGB: Boolean = true,
    showHex: Boolean = true
) {

    /* Loading dimension resources from dimens.xml */
    val hsvLabelMinWidth = dimensionResource(R.dimen.compose_hsv_label_min_width)
    val hsvLabelPaddingEnd = dimensionResource(R.dimen.compose_hsv_label_padding_end)
    val argbValueMinWidth = dimensionResource(R.dimen.compose_argb_value_min_width)
    val rowPadding = dimensionResource(R.dimen.compose_row_padding)
    val colPadding = dimensionResource(R.dimen.compose_col_padding)

    /* All the values are being initialized by the `initialColor`. */
    var currentColor: Int by remember { mutableStateOf(initialColor.hashCode()) }
    var currentAlpha: Float by remember { mutableStateOf(initialColor.alpha) }
    var currentHue: Float by remember { mutableStateOf(getHSV(initialColor)[0]) }
    var currentSat: Float by remember { mutableStateOf(getHSV(initialColor)[1]) }
    var currentVal: Float by remember { mutableStateOf(getHSV(initialColor)[2]) }

    /* The measured position of the SatValPainter. */
    var offsetSatVal by remember { mutableStateOf(Offset.Zero) }
    var sizeSatVal by remember { mutableStateOf(IntSize.Zero) }

    /* The measured position of the HuePainter. */
    var offsetHue by remember { mutableStateOf(Offset.Zero) }
    var sizeHue by remember { mutableStateOf(IntSize.Zero) }

    /* The measured position of the AlphaPainter. */
    var offsetAlpha by remember { mutableStateOf(Offset.Zero) }
    var sizeAlpha by remember { mutableStateOf(IntSize.Zero) }

    /* Callback listener */
    val listener: OnColorChangedListener? = onColorChanged

    /* Composable LocalContext */
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {

        /* HSV & ARGB */
        if (showHSV || showARGB) {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .padding(all = rowPadding * 4)
            ) {

                /* HSV */
                if (showHSV) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .padding(all = colPadding)
                            .weight(0.5F)
                    ) {
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(all = rowPadding)
                        ) {
                            Text(
                                text = stringResource(R.string.text_label_hue),
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .padding(end = hsvLabelPaddingEnd)
                                    .defaultMinSize(minWidth = hsvLabelMinWidth)
                                    .testTag("text_label_hue")
                            )
                            Text(
                                text = "$currentHue",
                                modifier = Modifier.testTag("value_hue")
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(all = rowPadding)
                        ) {
                            Text(
                                text = stringResource(R.string.text_label_sat),
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .padding(end = hsvLabelPaddingEnd)
                                    .defaultMinSize(minWidth = hsvLabelMinWidth)
                                    .testTag("text_label_sat")
                            )
                            Text(
                                text = "$currentSat",
                                modifier = Modifier.testTag("value_sat")
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(all = rowPadding)
                        ) {
                            Text(
                                text = stringResource(R.string.text_label_val),
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .padding(end = hsvLabelPaddingEnd)
                                    .defaultMinSize(minWidth = hsvLabelMinWidth)
                                    .testTag("text_label_val")
                            )
                            Text(
                                text = "$currentVal",
                                modifier = Modifier.testTag("value_val")
                            )
                        }
                    }
                }

                /* ARGB */
                if (showARGB) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .padding(all = colPadding)
                            .weight(0.5F)
                    ) {
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(all = rowPadding)
                        ) {
                            Text(
                                text = stringResource(R.string.text_label_alpha),
                                modifier = Modifier.testTag("text_label_alpha")
                            )
                            Text(
                                text = getChannelValue(currentColor, "alpha"),
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .defaultMinSize(minWidth = argbValueMinWidth)
                                    .testTag("value_alpha")
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(all = rowPadding)
                        ) {
                            Text(
                                text = stringResource(R.string.text_label_Blue),
                                modifier = Modifier.testTag("text_label_Blue")
                            )
                            Text(
                                text = getChannelValue(currentColor, "blue"),
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .defaultMinSize(minWidth = argbValueMinWidth)
                                    .testTag("value_blue")
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(all = rowPadding)
                        ) {
                            Text(
                                text = stringResource(R.string.text_label_red),
                                modifier = Modifier.testTag("text_label_red")
                            )
                            Text(
                                text = getChannelValue(currentColor, "red"),
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .defaultMinSize(minWidth = argbValueMinWidth)
                                    .testTag("value_red")
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(all = rowPadding)
                        ) {
                            Text(
                                text = stringResource(R.string.text_label_green),
                                modifier = Modifier.testTag("text_label_green")
                            )
                            Text(
                                text = getChannelValue(currentColor, "green"),
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .defaultMinSize(minWidth = argbValueMinWidth)
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
                    it.setAlpha(currentAlpha)
                    it.setHue(currentHue)
                    it.setSat(currentSat)
                    it.setValue(currentVal)
                },
                modifier = Modifier
                    .layoutId(SatVal)
                    .testTag("sat_val")
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { inputChange: PointerInputChange, _: Offset ->
                                val rect: RectF = getLayoutBounds(offsetSatVal, sizeSatVal)
                                currentSat = pointToSat(rect, inputChange.position.x)
                                currentVal = pointToVal(rect, inputChange.position.y)
                                currentColor = toIntColor(currentAlpha, currentHue, currentSat, currentVal)
                            }
                        )
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = { offset: Offset ->
                                val rect: RectF = getLayoutBounds(offsetSatVal, sizeSatVal)
                                currentSat = pointToSat(rect, offset.x)
                                currentVal = pointToVal(rect, offset.y)
                                currentColor = toIntColor(currentAlpha, currentHue, currentSat, currentVal)
                                println("Hue: $currentHue, Sat: $currentSat, Val: $currentVal (${offset.x.toInt()} x ${offset.y.toInt()})")
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
                        detectDragGestures(
                            onDrag = { inputChange: PointerInputChange, _: Offset ->
                                val rect: RectF = getLayoutBounds(offsetHue, sizeHue)
                                currentHue = pointToHue(rect, inputChange.position.y)
                                currentColor = toIntColor(currentAlpha, currentHue, currentSat, currentVal)
                            }
                        )
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = { offset: Offset ->
                                val rect: RectF = getLayoutBounds(offsetHue, sizeHue)
                                currentHue = pointToHue(rect, offset.y)
                                currentColor = toIntColor(currentAlpha, currentHue, currentSat, currentVal)
                                println("Val: $currentHue (${offset.y.toInt()})")
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
                        painter = AlphaPainter(Size(1014F, 80F)).also {
                            it.setAlphaByColor(currentColor)
                        },
                        modifier = Modifier
                            .layoutId(Alpha)
                            .testTag("alpha")
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDrag = { inputChange: PointerInputChange, _: Offset ->
                                        val rect: RectF = getLayoutBounds(offsetAlpha, sizeAlpha)
                                        currentAlpha = pointToAlpha(rect,  inputChange.position.x)
                                        currentColor = toIntColor(currentAlpha, currentHue, currentSat, currentVal)
                                    }
                                )
                            }
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = { offset: Offset ->
                                        val rect: RectF = getLayoutBounds(offsetAlpha, sizeAlpha)
                                        currentAlpha = pointToAlpha(rect, offset.x)
                                        currentColor = toIntColor(currentAlpha, currentHue, currentSat, currentVal)
                                        println("Alpha: $currentAlpha (${offset.x.toInt()})")
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
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier.weight(.46F),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    contentDescription = "Old Color",
                    contentScale = ContentScale.FillBounds,
                    painter = ColorPainter(Size(422F, 120F)).also {
                        it.setValue(initialColor)
                    },
                    modifier = Modifier
                        .layoutId(OldColor)
                        .testTag("old_color")
                        .padding(all = rowPadding)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = { _: Offset ->
                                    /* reset to the previous color */
                                    onButtonClick(context, OldColor, initialColor.hashCode(), listener)
                                    currentColor = initialColor.hashCode()
                                    currentAlpha = initialColor.alpha
                                    currentHue = getHSV(initialColor)[0]
                                    currentSat = getHSV(initialColor)[1]
                                    currentVal = getHSV(initialColor)[2]
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
                    painter = ColorPainter(Size(422F, 120F)).also {
                        it.setValue(currentColor)
                    },
                    modifier = Modifier
                        .layoutId(NewColor)
                        .testTag("new_color")
                        .padding(all = rowPadding)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = { _: Offset ->
                                    onButtonClick(
                                        context,
                                        NewColor,
                                        toIntColor(currentAlpha, currentHue, currentSat, currentVal),
                                        listener
                                    )
                                }
                            )
                        }
                )
            }
        }
    }
}

/** left, top, right, bottom */
fun getLayoutBounds(position: Offset, size: IntSize): RectF {
    return RectF(
        /* left */ position.x,
        /* top */ position.y,
        position.x + size.width.toFloat(),
        position.y + size.height.toFloat()
    )
}

/**
 * On button click.
 * @param context the Context of the Composable
 * @param layoutId either OldColor or NewColor
 * @param value the selected integer color value
 * @param listener {link OnColorChangedListener} or null
 */
fun onButtonClick(context: Context, layoutId: LayoutId, value: Int, listener: OnColorChangedListener?) {
    val message: String
    when (layoutId) {
        OldColor -> {
            message = "Old: ${toARGB(value)}"
        }
        NewColor -> {
            message = "New: ${toARGB(value)}"
            listener?.onColorChanged(value)
        }
        else -> { return }
    }
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    println(message)
}

/**
 * Initialize H/S/V "Float by remember" with a {@link Color}.
 * @param value the color to convert.
 * @return float-array HSV.
 */
fun getHSV(value: Color): FloatArray {
    val hsv = FloatArray(3)
    val red = value.red.times(255).toInt()
    val green = value.green.times(255).toInt()
    val blue = value.blue.times(255).toInt()
    RGBToHSV(red, green, blue, hsv)
    return hsv
}

/**
 * Convert H/S/V to Int.
 * @param alpha the alpha of the color.
 * @param hue the hue of the color.
 * @param sat the saturation of the color.
 * @param value the value of the color.
 * @return integer color value.
 */
fun toIntColor(alpha: Float, hue: Float, sat: Float, value: Float): Int {
    println("alpha: $alpha, hue: $hue, saturation: $sat, value: $value")
    return HSVToColor((alpha * 255).toInt(), floatArrayOf(hue, sat, value))
}

/**
 * Convert Int to A/R/G/B.
 * @param color the color value to convert.
 * @return an ARGB hexadecimal string.
 */
fun toARGB(color: Int): String {
    return String.format("#%1$02X", color)
}

/**
 * Alpha channel in numeric notation 0-255.
 * @param color the color value to convert.
 * @param name the name of the channel.
 * @return channel integer value as String.
 */
fun getChannelValue(color: Int, name: String): String {
    val value: Float = when (name) {
        "alpha" -> { Color(color).alpha }
        "red"   -> { Color(color).red   }
        "green" -> { Color(color).green }
        "blue"  -> { Color(color).blue  }
        else    -> { return "" }
    }
    return String.format("%s", value.times(255).toInt())
}

/**
 * @param rect boundaries of the hue rectangle.
 * @param y the relative position on the y axis.
 * @return hue 0.0F to 360.0F.
 */
private fun pointToHue(rect: RectF, y: Float): Float {
    return 360f - y * 360f / rect.height()
}

/**
 * @param rect boundaries of the sat/val rectangle.
 * @param x the relative position on the x axis.
 * @return sat 0.0F to 1.0F.
 */
private fun pointToSat(rect: RectF, x: Float): Float {
    return 1f / rect.width() * x
}

/**
 * @param rect boundaries of the sat/val rectangle.
 * @param y the relative position on the x axis.
 * @return value 0.0F to 1.0F.
 */
private fun pointToVal(rect: RectF, y: Float): Float {
    return 1f - 1f / rect.height() * y
}

/**
 * @param rect boundaries of the alpha rectangle.
 * @param x the relative position on the x axis.
 * @return alpha 0.0F to 1.0F.
 */
private fun pointToAlpha(rect: RectF, x: Float): Float {
    return x / rect.width()
}
