package io.syslogic.colorpicker.compose

import android.graphics.*
import android.graphics.Color.HSVToColor

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter

/**
 * Jetpack Compose Sat/Val Painter
 *
 * @author Martin Zeitler
 */
class SatValPainter(intrinsicSize: Size) : BasePainter(intrinsicSize) {

    private var value: FloatArray = FloatArray(4)

    /**
     * Implementation of drawing logic for instances of [Painter].
     * This is invoked internally within [draw] after the positioning and configuring the [Painter].
     */
    override fun DrawScope.onDraw() {

        setCanvas(drawContext, density)

        /* Saturation Shader */
        val mSaturationShader = LinearGradientShader (
            from = Offset(rect.right, rect.top),
            to = Offset(rect.left,  rect.top),
            tileMode = TileMode.Clamp,
            colors = listOf(
                Color(color = HSVToColor(getAlpha().times(255).toInt(), floatArrayOf(getHue(), 1.0F, 1.0F))),
                Color.White
            )
        )

        /* Value Shader */
        val mContrastShader = LinearGradientShader(
            from = Offset(rect.left, rect.bottom),
            to = Offset(rect.left, rect.top),
            tileMode = TileMode.Clamp,
            colors = listOf(
                Color.Black,
                Color.White
            )
        )

        /* Compose Shader */
        val paint = Paint()
        paint.shader = ComposeShader(mContrastShader, mSaturationShader, PorterDuff.Mode.MULTIPLY)
        canvas.drawRect(rect, paint)

        /* Borderline Paint */
        val border = Paint()
        border.style = Paint.Style.STROKE
        border.strokeWidth = borderStrokeWidth
        border.color = borderStrokeColor

        /* Borderline */
        canvas.drawRect(rect, border)

        /* Tracker Paint */
        val tracker = Paint()
        tracker.style = Paint.Style.STROKE
        tracker.strokeWidth = trackerStrokeWidth
        tracker.color = trackerStrokeColor
        tracker.isAntiAlias = true

        /* Circular Tracker */
        val p: Point = satValToPoint(getSat(), getValue())
        canvas.drawCircle(p.x.toFloat(), p.y.toFloat(), satValTrackerRadius, tracker)
    }

    private fun getHue() : Float {
        return this.value[0]
    }

    private fun getSat() : Float {
        return this.value[1]
    }

    private fun getValue() : Float {
        return this.value[2]
    }

    private fun getAlpha() : Float {
        return this.value[3]
    }

    fun setHue(value: Float) {
        this.value[0] = value
    }

    fun setSat(value: Float) {
        this.value[1] = value
    }

    fun setValue(value: Float) {
        this.value[2] = value
    }

    fun setAlpha(value: Float) {
        this.value[3] = value
    }
}
