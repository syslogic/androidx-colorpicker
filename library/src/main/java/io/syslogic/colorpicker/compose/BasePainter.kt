package io.syslogic.colorpicker.compose

import android.graphics.Rect
import android.graphics.RectF

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.NativeCanvas
import androidx.compose.ui.graphics.drawscope.DrawContext
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter

/**
 * Jetpack Compose Base Painter
 *
 * @author Martin Zeitler
 */
abstract class BasePainter(override val intrinsicSize: Size) : Painter() {
    protected lateinit var canvas: NativeCanvas
    protected lateinit var rect: RectF
    lateinit var bounds: Rect
    val borderWidth: Float = 2F
    val borderColor: Int = -0x919192
    fun setCanvas(drawContext: DrawContext) {
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
