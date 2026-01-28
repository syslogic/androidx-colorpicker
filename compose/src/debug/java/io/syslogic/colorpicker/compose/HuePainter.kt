package io.syslogic.colorpicker.compose

import android.graphics.Point
import androidx.compose.ui.geometry.Offset

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter

/**
 * Jetpack Compose Hue Painter
 *
 * @author Martin Zeitler
 */
class HuePainter(intrinsicSize: Size) : BasePainter(intrinsicSize) {

    private var value: Float = 360f

    /**
     * Implementation of drawing logic for instances of [Painter].
     * This is invoked internally within [draw] after the positioning and configuring the [Painter].
     */
    override fun DrawScope.onDraw() {
        setCanvas(drawContext, density)
        drawRect(
            size = size,
            brush = Brush.verticalGradient(
                colors = getHueValues()
            )
        ).also {

            /* Border */
            drawRect(
                size = size,
                color = Color(borderStrokeColor),
                topLeft = Offset(outerRect.left, outerRect.top),
                style = Stroke(width = borderStrokeWidth,
                    pathEffect = PathEffect.cornerPathEffect(borderCornerRadius)
                )
            )

            /* Vertical Tracker */
            val p: Point = hueToPoint(value)
            val offset = Offset(innerRect.left, p.y - (hueTrackerHeight / 2))
            drawRoundRect(
                color = Color(trackerStrokeColorOuter),
                size = Size(innerRect.width(), hueTrackerHeight),
                topLeft = offset,
                style = Stroke(width = trackerStrokeWidth,
                    pathEffect = PathEffect.cornerPathEffect(trackerCornerRadius)
                )
            )
        }
    }

    private fun getHueValues(): List<Color> {
        var i = 0
        val list: MutableList<Color> = MutableList(360) { Color.Black }
        while (i < list.size) {
            val value = (list.size - i).toFloat()
            list[i] = Color(android.graphics.Color.HSVToColor(255, floatArrayOf(value, 1F, 1F)))
            i++
        }
        return list
    }

    fun setValue(value: Float) {
        this.value = value
    }
}
