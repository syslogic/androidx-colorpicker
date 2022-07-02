package io.syslogic.colorpicker.compose

import android.graphics.Rect
import android.graphics.RectF

import androidx.compose.ui.geometry.Size
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

    /* Trackers */
    var trackerStrokeRadius by Delegates.notNull<Float>()
    var trackerStrokeWidth by Delegates.notNull<Float>()
    var trackerStrokeColor by Delegates.notNull<Int>()

    /* Borders */
    var borderStrokeRadius by Delegates.notNull<Float>()
    var borderStrokeWidth  by Delegates.notNull<Float>()
    var borderStrokeColor  by Delegates.notNull<Int>()

    /* Canvas */
    protected lateinit var canvas: NativeCanvas
    protected lateinit var rect: RectF
    lateinit var bounds: Rect

    fun setCanvas(drawContext: DrawContext, density: Float) {

        /* Trackers */
        trackerStrokeWidth = 1F * density
        trackerStrokeRadius = 2F * density
        trackerStrokeColor = -0xe3e3e4

        /* Borders */
        borderStrokeWidth = 2F * density
        borderStrokeRadius = 2F * density
        borderStrokeColor = -0x919192

        /* Canvas */
        canvas = drawContext.canvas.nativeCanvas
        bounds = canvas.clipBounds
        rect = RectF(
            bounds.left.toFloat(),
            bounds.top.toFloat(),
            bounds.right.toFloat(),
            bounds.bottom.toFloat()
        )
    }
}
