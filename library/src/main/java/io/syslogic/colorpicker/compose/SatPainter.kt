package io.syslogic.colorpicker.compose

import android.graphics.Point
import android.graphics.RectF
import androidx.compose.ui.geometry.Offset

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter

/**
 * Jetpack Compose Sat Painter
 *
 * @author Martin Zeitler
 */
class SatPainter(override val intrinsicSize: Size) : Painter() {

    private var borderRadius: Float = 2F
    private var value: FloatArray = FloatArray(2)
    private lateinit var mRect: RectF

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

            /* Tracker */
            val p: Point = valueToPoint(value[0], value[1])
            val rectHeight: Float = 4 * density
            val offset = Offset(mRect.left, p.y - (rectHeight / 2));

            drawRoundRect(
                color = Color.Black,
                size = Size(mRect.width(), rectHeight),
                topLeft = offset,
                style = Stroke(width = 4f,
                    pathEffect = PathEffect.cornerPathEffect(borderRadius)
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

    private fun valueToPoint(saturation: Float, contrast: Float): Point {
        val height = mRect.height()
        val width = mRect.width()
        val p = Point()
        p.x = (saturation * width + mRect.left).toInt()
        p.y = ((1f - contrast) * height + mRect.top).toInt()
        return p
    }

    fun setValue(sat: Float, value: Float) {
        this.value[0] = sat
        this.value[1] = value
    }
}
