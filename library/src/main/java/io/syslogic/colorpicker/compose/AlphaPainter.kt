package io.syslogic.colorpicker.compose

import android.graphics.Point
import android.graphics.RectF
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import kotlin.properties.Delegates

/**
 * Jetpack Compose Alpha Painter
 *
 * @author Martin Zeitler
 */
class AlphaPainter(override val intrinsicSize: Size) : Painter() {

    private var value: Float = 1F
    private var trackerWidth by Delegates.notNull<Float>()
    private var borderRadius: Float = 2F
    private lateinit var mRect: RectF

    /**
     * Implementation of drawing logic for instances of [Painter]. This is invoked
     * internally within [draw] after the positioning and configuring the [Painter]
     */
    override fun DrawScope.onDraw() {
        trackerWidth = 2 * density
        drawRect(
            size = size,
            brush = Brush.linearGradient(
                colors = listOf(Color.Transparent, Color.Black)
            )
        ).also {

            /* required for setting the initial value */
            val canvas = drawContext.canvas.nativeCanvas
            val bounds = canvas.clipBounds
            mRect = RectF(
                bounds.left.toFloat(),
                bounds.top.toFloat(),
                bounds.right.toFloat(),
                bounds.bottom.toFloat()
            )

            /* Tracker */
            val p: Point = valueToPoint(value)

            // TODO:
            val offset = Offset(p.x - (trackerWidth / 2), mRect.height())

            drawRoundRect(
                color = Color.Black,
                size = Size(trackerWidth, mRect.height()),
                topLeft = offset,
                style = Stroke(width = 4f,
                    pathEffect = PathEffect.cornerPathEffect(borderRadius)
                )
            )
        }
    }

    private fun valueToPoint(value: Float): Point {
        val width = mRect.width()
        val p = Point()
        p.x = (width - value * width / 0xff + mRect.left).toInt()
        p.y = mRect.top.toInt()
        return p
    }

    fun setAlpha(color: Int) {
        this.value = Color(color).alpha
    }
}
