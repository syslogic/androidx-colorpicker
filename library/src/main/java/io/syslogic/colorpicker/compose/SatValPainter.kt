package io.syslogic.colorpicker.compose

import android.graphics.Color.HSVToColor
import android.graphics.ComposeShader
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PorterDuff
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

        /* Saturation Shader with Alpha */
        val alpha: Int = getAlpha().times(255).toInt()
        val color: Int = HSVToColor(alpha, floatArrayOf(getHue(), 1F, 1F))
        val mSaturationShader = LinearGradientShader (
            from = Offset(rect.right, rect.top),
            to = Offset(rect.left,  rect.top),
            tileMode = TileMode.Clamp,
            colors = listOf(Color(color), Color.White)
        )

        /* Value Shader */
        val mContrastShader = LinearGradientShader(
            from = Offset(rect.left, rect.bottom),
            to = Offset(rect.left, rect.top),
            tileMode = TileMode.Clamp,
            colors = listOf(Color.Black, Color.White)
        )

        /* Compose Shader */
        val composition = Paint()
        composition.shader = ComposeShader(mContrastShader, mSaturationShader, PorterDuff.Mode.MULTIPLY)
        canvas.drawRect(rect, composition)

        /* Border */
        val border = Paint()
        border.style = Paint.Style.STROKE
        border.strokeWidth = borderStrokeWidth
        border.color = borderStrokeColor
        canvas.drawRect(outline, border)

        /* Tracker */
        val p: Point = satValToPoint(getSat(), getValue())
        val tracker = Paint()
        tracker.style = Paint.Style.STROKE
        tracker.strokeWidth = trackerStrokeWidth
        tracker.isAntiAlias = true

        /* Outer circle (dark) */
        tracker.color = trackerStrokeColor
        canvas.drawCircle(p.x.toFloat(), p.y.toFloat(), satValTrackerRadius1, tracker)

        /* Inner circle (light) */
        tracker.color = Color.White.hashCode()
        canvas.drawCircle(p.x.toFloat(), p.y.toFloat(), satValTrackerRadius2, tracker)
    }

    /** Getters */
    private fun getAlpha() : Float {
        return this.value[0]
    }
    private fun getHue() : Float {
        return this.value[1]
    }
    private fun getSat() : Float {
        return this.value[2]
    }
    private fun getValue() : Float {
        return this.value[3]
    }

    /** Setters */
    fun setAlpha(value: Float) {
        this.value[0] = value
    }
    fun setHue(value: Float) {
        this.value[1] = value
    }
    fun setSat(value: Float) {
        this.value[2] = value
    }
    fun setValue(value: Float) {
        this.value[3] = value
    }
}
