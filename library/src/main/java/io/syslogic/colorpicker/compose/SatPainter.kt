package io.syslogic.colorpicker.compose

import android.graphics.Point
import android.graphics.RectF
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter

/**
 * Jetpack Compose Sat Painter
 *
 * @author Martin Zeitler
 */
class SatPainter(override val intrinsicSize: Size) : Painter() {

    private lateinit var mRect: RectF
    private var color: Int = Color.Black.hashCode()

    /**
     * Implementation of drawing logic for instances of [Painter]. This is invoked
     * internally within [draw] after the positioning and configuring the [Painter]
     */
    override fun DrawScope.onDraw() {
        drawRect(
            size = size,
            brush = Brush.verticalGradient(
                colors = getValues()
            )
        ).also {

            /* required for setting the initial value */
            val canvas = drawContext.canvas.nativeCanvas
            val bounds = canvas.clipBounds
            mRect = RectF(
                bounds.left.toFloat(),
                bounds.top.toFloat(),
                bounds.right.toFloat(),
                bounds.bottom.toFloat()
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

    private fun valueToPoint(sat: Float, value: Float): Point {
        val height = mRect.height()
        val width = mRect.width()
        val p = Point()
        p.x = (sat * width + mRect.left).toInt()
        p.y = ((1f - value) * height + mRect.top).toInt()
        return p
    }

    fun setColor(value: Int) {
        this.color = value
    }
}
