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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
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
    initialColor: Int = Color.Transparent.hashCode(),
    onColorChanged: OnColorChangedListener?,
    showAlphaSlider: Boolean = false,
    showHexValue: Boolean = true
) {
    val context = LocalContext.current
    val rowPadding = dimensionResource(R.dimen.compose_row_padding)

    var currentColor: Int by remember { mutableStateOf(initialColor) }
    var currentAlpha: Int by remember { mutableStateOf(255) }
    var currentHue: Float by remember { mutableStateOf(0F) }
    var currentSat: Float by remember { mutableStateOf(0F) }
    var currentVal: Float by remember { mutableStateOf(0F) }

    @Suppress("UNUSED_VARIABLE")
    var listener: OnColorChangedListener? = onColorChanged

    var offsetHue by remember { mutableStateOf(Offset.Zero) }
    var sizeHue by remember { mutableStateOf(IntSize.Zero) }

    var offsetSat by remember { mutableStateOf(Offset.Zero) }
    var sizeSat by remember { mutableStateOf(IntSize.Zero) }

    var offsetAlpha by remember { mutableStateOf(Offset.Zero) }
    var sizeAlpha by remember { mutableStateOf(IntSize.Zero) }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
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
                modifier = Modifier
                    .layoutId(Hue)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = { event ->
                                currentHue = pointToHue(getLayoutBounds(offsetHue, sizeHue), event.y)
                                log(context,"Hue: ${currentHue}\n${event.x.toInt()} x ${event.y.toInt()}")
                                toColor(currentAlpha, currentHue, currentSat, currentVal)
                            }
                        )
                    }
                    .onGloballyPositioned { coordinates ->
                        offsetHue = coordinates.positionInRoot()
                        sizeHue = coordinates.size
                    }
            )

            Spacer(
                modifier = Modifier.padding(all = rowPadding)
            )

            /* Sat Panel */
            Image(
                contentDescription = "Saturation",
                contentScale = ContentScale.FillBounds,
                painter = SatPainter(Size(100F, 900F)).also {
                    it.setColor(initialColor)
                },
                modifier = Modifier
                    .layoutId(Sat)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = { event ->
                                currentSat = pointToSat(getLayoutBounds(offsetSat, sizeSat), event.x, event.y)
                                log(context,"Sat: ${currentSat}\n${event.x.toInt()} x ${event.y.toInt()}")
                            }
                        )
                    }
                    .onGloballyPositioned { coordinates ->
                        offsetSat = coordinates.positionInRoot()
                        sizeSat = coordinates.size
                    }
            )
        }

        /* Alpha Panel */
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
                        modifier = Modifier
                            .layoutId(Alpha)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = { event ->
                                        currentAlpha = pointToAlpha(getLayoutBounds(offsetAlpha, sizeAlpha), event.x.toInt())
                                        log(context,"Alpha: ${currentAlpha}\n${event.x.toInt()} x ${event.y.toInt()}")
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
                    painter = ColorPainter(Size(400F, 120F)).also {
                        it.setColor(initialColor)
                    },
                    modifier = Modifier
                        .padding(all = rowPadding)
                        .layoutId(OldColor)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    onButtonClick(context, OldColor)
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
                    painter = ColorPainter(Size(400F, 120F)).also {
                        it.setColor(currentColor)
                    },
                    modifier = Modifier
                        .padding(all = rowPadding)
                        .layoutId(NewColor)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    onButtonClick(context, NewColor)
                                }
                            )
                        }
                )
            }
        }
    }
}

fun log(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    println(message)
}

fun onButtonClick(context: Context, layoutId: LayoutId) {
    val message: String = when (layoutId) {
        OldColor -> {
            "OldColor: ?"
        }
        NewColor -> {
            "NewColor: ?"
        }
        else -> {
            return
        }
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

/**
 * @param color the color value to convert.
 */
fun convertToARGB(color: Int): String {
    var alpha = Integer.toHexString(Color(color).alpha.toInt()).uppercase(Locale.ROOT)
    var red = Integer.toHexString(Color(color).red.toInt()).uppercase(Locale.ROOT)
    var green = Integer.toHexString(Color(color).green.toInt()).uppercase(Locale.ROOT)
    var blue = Integer.toHexString(Color(color).blue.toInt()).uppercase(Locale.ROOT)
    if (alpha.length == 1) {alpha = String.format("0%s", alpha)}
    if (red.length   == 1) {red   = String.format("0%s", red)}
    if (green.length == 1) {green = String.format("0%s", green)}
    if (blue.length  == 1) {blue  = String.format("0%s", blue)}
    return String.format("#%s%s%s%s", alpha, red, green, blue)
}

fun toColor(alpha: Int, hue: Float, sat: Float, value: Float) : Int {
    return android.graphics.Color.HSVToColor(alpha, floatArrayOf(hue, sat, value))
}

private fun pointToHue(rect: RectF, y: Float): Float {
    val height = rect.height()
    val y2 = if (y < rect.top) { 0f }
    else if (y > rect.bottom) { height }
    else { y - rect.top }
    return 360f - y2 * 360f / height
}

private fun pointToSat(rect: RectF, x: Float, y: Float): Float {

    val result = FloatArray(2)
    val width = rect.width()
    val height = rect.height()

    val x2 = if (x < rect.left) { 0f }
    else if (x > rect.right) { width }
    else { x - rect.left }

    val y2 = if (y < rect.top) { 0f }
    else if (y > rect.bottom) { height }
    else { y - rect.top }

    result[0] = 1f / width * x2
    result[1] = 1f - 1f / height * y2
    return result[1]
}

private fun pointToAlpha(rect: RectF, x: Int): Int {
    val width = rect.width().toInt()
    val x2 = if (x < rect.left) { 0 }
    else if (x > rect.right) { width }
    else { x - rect.left.toInt() }
    return 0xff - x2 * 0xff / width
}
