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

import kotlin.properties.Delegates

/**
 * Jetpack Compose Hue Painter
 *
 * @author Martin Zeitler
 */
class HuePainter(intrinsicSize: Size) : BasePainter(intrinsicSize) {

    private var trackerHeight by Delegates.notNull<Float>()
    private var value: Float = 360f

    /**
     * Implementation of drawing logic for instances of [Painter]. This is invoked
     * internally within [draw] after the positioning and configuring the [Painter]
     */
    override fun DrawScope.onDraw() {
        trackerHeight = 4F * density
        setCanvas(drawContext, density)

        drawRect(
            size = size,
            brush = Brush.verticalGradient(
                colors = getValues()
            )
        ).also {

            /* Border */
            drawRect(
                color = Color(borderStrokeColor),
                size = size,
                topLeft = Offset(bounds.left.toFloat(), bounds.top.toFloat()),
                style = Stroke(width = borderStrokeWidth,
                    pathEffect = PathEffect.cornerPathEffect(borderStrokeRadius)
                )
            )

            /* Tracker */
            val p: Point = valueToPoint(value)
            val offset = Offset(rect.left, p.y - (trackerHeight / 2))
            drawRoundRect(
                color = Color(trackerStrokeColor),
                size = Size(rect.width(), trackerHeight),
                topLeft = offset,
                style = Stroke(width = trackerStrokeWidth,
                    pathEffect = PathEffect.cornerPathEffect(trackerStrokeRadius)
                )
            )
        }
    }

    private fun getValues(): List<Color> {
        var i = 0
        val list: MutableList<Color> = MutableList(361) { Color.Black }
        while (i < list.size) {
            val value = (list.size - i).toFloat()
            list[i] = Color(android.graphics.Color.HSVToColor(floatArrayOf(value, 1f, 1f)))
            i++
        }
        return list
    }

    private fun valueToPoint(value: Float): Point {
        val height = rect.height()
        val p = Point()
        p.y = (height - value * height / 360f + rect.top).toInt()
        p.x = rect.left.toInt()
        return p
    }

    fun setValue(value: Float) {
        this.value = value
    }
}
