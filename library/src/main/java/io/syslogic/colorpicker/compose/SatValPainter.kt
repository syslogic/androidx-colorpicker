package io.syslogic.colorpicker.compose

import android.graphics.*
import android.graphics.Color.HSVToColor

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter

/**
 * Jetpack Compose Sat/Val Painter
 *
 * @author Martin Zeitler
 */
class SatValPainter(intrinsicSize: Size) : BasePainter(intrinsicSize) {

    private var value: FloatArray = FloatArray(2)
    private var hue: Float = 360F

    /**
     * Implementation of drawing logic for instances of [Painter].
     * This is invoked internally within [draw] after the positioning and configuring the [Painter].
     */
    override fun DrawScope.onDraw() {

        setCanvas(drawContext, density)

        /* Saturation Shader */
        val mSaturationShader = LinearGradientShader(
            from     = Offset(rect.right, rect.top),
            to       = Offset(rect.left, rect.top),
            tileMode = androidx.compose.ui.graphics.TileMode.Clamp,
            colors   = listOf(Color(HSVToColor(floatArrayOf(hue, 1f, 1f))), Color.White)
        )

        /* Contrast Shader */
        val mContrastShader = LinearGradientShader(
            from     = Offset(rect.left, rect.bottom),
            to       = Offset(rect.left, rect.top),
            tileMode = androidx.compose.ui.graphics.TileMode.Clamp,
            colors   = listOf(Color.Black, Color.White)
        )

        /* Compose Shader */
        val paint = Paint()
        paint.shader = ComposeShader(mContrastShader, mSaturationShader, PorterDuff.Mode.MULTIPLY)
        canvas.drawRect(rect, paint)

        /* TODO: How about border paint? */

        /* Tracker Paint */
        val tracker = Paint()
        tracker.style = Paint.Style.STROKE
        tracker.strokeWidth = trackerStrokeWidth
        tracker.color = trackerStrokeColor
        tracker.isAntiAlias = true

        /* Circular Tracker */
        val p: Point = valueToPoint(value[0], value[1])
        canvas.drawCircle(p.x.toFloat(), p.y.toFloat(), satValTrackerRadius, tracker)
    }

    private fun valueToPoint(saturation: Float, value: Float): Point {
        return Point(
            (saturation * rect.width() + rect.left).toInt(),
            ((1f - value) * rect.height() + rect.top).toInt()
        )
    }

    fun setValue(saturation: Float, value: Float) {
        this.value[0] = saturation
        this.value[1] = value
    }

    fun setHue(value: Float) {
        this.hue = value
    }
}
