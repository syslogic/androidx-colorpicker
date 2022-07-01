package io.syslogic.colorpicker.compose

import android.graphics.*
import android.graphics.Color.HSVToColor

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter

/**
 * Jetpack Compose Sat/Val Painter
 *
 * @author Martin Zeitler
 */
class SatValPainter(override val intrinsicSize: Size) : Painter() {

    private var value: FloatArray = FloatArray(2)
    private var trackerRadius: Float = 20F
    private lateinit var mRect: RectF

    /**
     * Implementation of drawing logic for instances of [Painter].
     * This is invoked internally within [draw] after the positioning and configuring the [Painter].
     */
    override fun DrawScope.onDraw() {

        /* required for setting the initial value */
        val canvas = drawContext.canvas.nativeCanvas
        val bounds = canvas.clipBounds
        mRect = RectF(
            bounds.left.toFloat(),
            bounds.top.toFloat(),
            bounds.right.toFloat(),
            bounds.bottom.toFloat()
        )

        /* Saturation Shader */
        val mSaturationShader = LinearGradientShader(
            from     = Offset(mRect.right, mRect.top),
            to       = Offset(mRect.left, mRect.top),
            tileMode = androidx.compose.ui.graphics.TileMode.Clamp,
            colors   = getValues()
        )

        /* Contrast Shader */
        val mContrastShader = LinearGradientShader(
            from     = Offset(mRect.left, mRect.bottom),
            to       = Offset(mRect.left, mRect.top),
            tileMode = androidx.compose.ui.graphics.TileMode.Clamp,
            colors   = listOf(Color.Black, Color.White)
        )

        /* Compose Shader */
        val paint = Paint()
        paint.shader = ComposeShader(mContrastShader, mSaturationShader, PorterDuff.Mode.MULTIPLY)
        canvas.drawRect(mRect, paint)

        /* Tracker */
        val tracker = Paint()
        tracker.style = Paint.Style.STROKE
        tracker.strokeWidth = 2f * density
        tracker.isAntiAlias = true
        tracker.color = -0xe3e3e4

        /* Tracker */
        val p: Point = valueToPoint(value[0], value[1])
        canvas.drawCircle(p.x.toFloat(), p.y.toFloat(), trackerRadius, tracker)
    }

    private fun getValues(): List<Color> {
        val list: MutableList<Color> = MutableList(361) { Color.Black }
        val arr = IntArray(361)
        var i = arr.size - 1
        while (i >= 0) {
            list[i] = Color(HSVToColor(floatArrayOf(i.toFloat(), 1f, 1f)))
            i--
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

    fun setValue(sat: Float, value: Float) {
        this.value[0] = sat
        this.value[1] = value
    }
}
