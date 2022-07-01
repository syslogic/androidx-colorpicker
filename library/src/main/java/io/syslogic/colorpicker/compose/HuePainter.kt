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
 * Jetpack Compose Hue Painter
 *
 * @author Martin Zeitler
 */
class HuePainter(override val intrinsicSize: Size) : Painter() {

    private lateinit var mRect: RectF
    private var radius: Float = 20F

    private var value: Float = 360f

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
        tracker.isAntiAlias = true
        tracker.color = -0xe3e3e4
        tracker.style = Paint.Style.STROKE
        tracker.strokeWidth = 4f * density

        /* Tracker */
        val p: Point = valueToPoint(value)
        canvas.drawCircle(p.x.toFloat(), p.y.toFloat(), radius, tracker)
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

    private fun valueToPoint(hue: Float): Point {
        val height = mRect.height()
        val p = Point()
        p.y = (height - hue * height / 360f + mRect.top).toInt()
        p.x = mRect.left.toInt()
        return p
    }

    fun setValue(value: Float) {
        this.value = value
    }
}
