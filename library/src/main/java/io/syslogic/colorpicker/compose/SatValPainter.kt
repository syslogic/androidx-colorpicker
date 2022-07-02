package io.syslogic.colorpicker.compose

import android.graphics.*
import android.graphics.Color.HSVToColor

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter

import kotlin.properties.Delegates

/**
 * Jetpack Compose Sat/Val Painter
 *
 * @author Martin Zeitler
 */
class SatValPainter(intrinsicSize: Size) : BasePainter(intrinsicSize) {

    private var trackerRadius by Delegates.notNull<Float>()
    private var value: FloatArray = FloatArray(2)
    private var hue: Float = 360F

    /**
     * Implementation of drawing logic for instances of [Painter].
     * This is invoked internally within [draw] after the positioning and configuring the [Painter].
     */
    override fun DrawScope.onDraw() {

        trackerRadius = 8F * density
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

        /* Tracker Paint */
        val tracker = Paint()
        tracker.style = Paint.Style.STROKE
        tracker.strokeWidth = trackerStrokeWidth
        tracker.color = trackerStrokeColor
        tracker.isAntiAlias = true

        /* Tracker */
        val p: Point = valueToPoint(value[0], value[1])
        canvas.drawCircle(p.x.toFloat(), p.y.toFloat(), trackerRadius, tracker)
    }

    private fun valueToPoint(sat: Float, value: Float): Point {
        val height = rect.height()
        val width = rect.width()
        val p = Point()
        p.x = (sat * width + rect.left).toInt()
        p.y = ((1f - value) * height + rect.top).toInt()
        return p
    }

    fun setValue(sat: Float, value: Float) {
        this.value[0] = sat
        this.value[1] = value
    }
    fun setHue(value: Float) {
        this.hue = value
    }
}
