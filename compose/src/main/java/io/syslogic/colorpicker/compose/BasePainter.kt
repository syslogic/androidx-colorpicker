package io.syslogic.colorpicker.compose

import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.NativeCanvas
import androidx.compose.ui.graphics.drawscope.DrawContext
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter

import kotlin.properties.Delegates

/**
 * Jetpack Compose Base Painter
 *
 * @author Martin Zeitler
 */
abstract class BasePainter(override val intrinsicSize: Size) : Painter() {

    /** Border Style: Corner Radius */
    var borderCornerRadius by Delegates.notNull<Float>()

    /** Border Style: Stroke Width */
    var borderStrokeWidth by Delegates.notNull<Float>()

    /** Border Style: Stroke Color */
    var borderStrokeColor by Delegates.notNull<Int>()

    /** Tracker Dimension: Sat/Val Tracker Radius */
    var satValTrackerRadius1 by Delegates.notNull<Float>()
    var satValTrackerRadius2 by Delegates.notNull<Float>()

    /** Tracker Dimension: Alpha Tracker Width */
    var alphaTrackerWidth by Delegates.notNull<Float>()

    /** Tracker Dimension: Hue Tracker Height */
    var hueTrackerHeight by Delegates.notNull<Float>()

    /** Tracker Style: Corner Radius */
    var trackerCornerRadius by Delegates.notNull<Float>()

    /** Tracker Style: Stroke Width */
    var trackerStrokeWidth by Delegates.notNull<Float>()

    /** Tracker Style: Outer Stroke Color */
    var trackerStrokeColorOuter by Delegates.notNull<Int>()

    /** Tracker Style: Inner Stroke Color */
    var trackerStrokeColorInner by Delegates.notNull<Int>()

    /** Native Canvas */
    protected lateinit var canvas: NativeCanvas

    /** Boundaries */
    protected lateinit var outerRect: RectF
    protected lateinit var innerRect: RectF

    /** Being called by all implementations onDraw. */
    fun setCanvas(drawContext: DrawContext, density: Float) {

        /* Tracker Dimensions */
        satValTrackerRadius1 = 9F * density
        satValTrackerRadius2 = 8F * density
        alphaTrackerWidth = 6F * density
        hueTrackerHeight = 4F * density

        /* Tracker Styles */
        trackerStrokeColorOuter = Color.Black.hashCode()
        trackerStrokeColorInner = Color.White.hashCode()
        trackerCornerRadius = 2F * density
        trackerStrokeWidth = 1F * density

        /* Border Styles */
        borderCornerRadius = 2F
        borderStrokeWidth = 2F
        borderStrokeColor = Color.DarkGray.hashCode()

        /* Native Canvas */
        canvas = drawContext.canvas.nativeCanvas
        val bounds: Rect = canvas.clipBounds
        outerRect = RectF(
            bounds.left.toFloat(),
            bounds.top.toFloat(),
            bounds.right.toFloat(),
            bounds.bottom.toFloat()
        )
        innerRect = RectF(
            bounds.left.toFloat() + borderStrokeWidth,
            bounds.top.toFloat() + borderStrokeWidth,
            bounds.right.toFloat() - borderStrokeWidth,
            bounds.bottom.toFloat() - borderStrokeWidth
        )
    }

    /**
     * AlphaPainter {@link Float} to {@link Point} value conversion.
     * @param value a Float representation of the current alpha.
     * @return the {@link Point} where AlphaPainter shall draw the tracker rectangle.
     */
    fun alphaToPoint(value: Float): Point {
        val x: Int = (value * innerRect.width()).toInt()
        val y: Int = innerRect.top.toInt()
        return Point(x, y)
    }

    /**
     * HuePainter {@link Float} to {@link Point} value conversion.
     * @param value a Float representation of the current hue.
     * @return the {@link Point} where HuePainter shall draw the tracker rectangle.
     */
    fun hueToPoint(value: Float): Point {
        val x: Int = innerRect.left.toInt()
        val y: Int = (innerRect.height() - value * innerRect.height() / 360f + innerRect.top).toInt()
        return Point(x, y)
    }

    /**
     * SatValPainter {@link Float} to {@link Point} value conversion.
     * @param saturation a Float representation of the current saturation.
     * @param value a Float representation of the current value.
     * @return the Point where SatValPainter shall draw the tracker circle.
     */
    fun satValToPoint(saturation: Float, value: Float): Point {
        val x: Int = (saturation * innerRect.width() + innerRect.left).toInt()
        val y: Int = ((1f - value) * innerRect.height() + innerRect.top).toInt()
        return Point(x, y)
    }
}
