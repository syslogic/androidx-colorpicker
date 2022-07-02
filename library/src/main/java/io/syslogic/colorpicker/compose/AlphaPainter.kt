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
 * Jetpack Compose Alpha Painter
 *
 * @author Martin Zeitler
 */
class AlphaPainter(intrinsicSize: Size) : BasePainter(intrinsicSize) {

    private var trackerWidth by Delegates.notNull<Float>()

    private var value: Float = 1.0F

    /**
     * Implementation of drawing logic for instances of [Painter]. This is invoked
     * internally within [draw] after the positioning and configuring the [Painter]
     */
    override fun DrawScope.onDraw() {
        trackerWidth = 4F * density
        setCanvas(drawContext, density)

        drawRect(
            size = size,
            brush = Brush.linearGradient(
                colors = listOf(Color.Transparent, Color.Black)
            )
        ).also {

            /* Border */
            var offset = Offset(bounds.left.toFloat(), bounds.top.toFloat())
            drawRect(
                color = Color(borderStrokeColor),
                size = size,
                topLeft = offset,
                style = Stroke(width = borderStrokeWidth,
                    pathEffect = PathEffect.cornerPathEffect(borderStrokeRadius)
                )
            )

            /* Tracker */
            val p: Point = valueToPoint(value)
            offset = Offset(p.x - (trackerWidth / 2), rect.top)

            drawRoundRect(
                color = Color(trackerStrokeColor),
                size = Size(trackerWidth, rect.height()),
                topLeft = offset,
                style = Stroke(width = trackerStrokeWidth,
                    pathEffect = PathEffect.cornerPathEffect(trackerStrokeRadius)
                )
            )
        }
    }

    private fun valueToPoint(value: Float): Point {
        val width = rect.width()
        val p = Point()
        p.x = (value * width).toInt()
        p.y = rect.top.toInt()
        return p
    }

    fun setAlphaByColor(color: Int) {
        this.value = Color(color).alpha
    }
}
