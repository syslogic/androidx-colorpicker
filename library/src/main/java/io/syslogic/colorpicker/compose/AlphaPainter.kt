package io.syslogic.colorpicker.compose

import android.graphics.Point
import android.graphics.RectF
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter

class AlphaPainter(override val intrinsicSize: Size) : Painter() {

    private lateinit var mRect: RectF
    private var alpha: Float = 1F

    /**
     * Implementation of drawing logic for instances of [Painter]. This is invoked
     * internally within [draw] after the positioning and configuring the [Painter]
     */
    override fun DrawScope.onDraw() {

        val canvas = drawContext.canvas.nativeCanvas
        val bounds = canvas.clipBounds
        mRect = RectF(
            bounds.left.toFloat(),
            bounds.top.toFloat(),
            bounds.right.toFloat(),
            bounds.bottom.toFloat()
        )

        drawRect(
            size = size,
            brush = Brush.linearGradient(
                colors = listOf(Color.Transparent, Color.Black)
            )
        )
    }

    @Suppress("unused")
    private fun valueToPoint(value: Float): Point {
        val width = mRect.width()
        val p = Point()
        p.x = (width - value * width / 0xff + mRect.left).toInt()
        p.y = mRect.top.toInt()
        return p
    }

    fun setAlpha(color: Int) {
        this.alpha = Color(color).alpha
    }
}
