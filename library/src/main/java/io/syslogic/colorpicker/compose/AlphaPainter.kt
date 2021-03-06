package io.syslogic.colorpicker.compose

import android.graphics.Point

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter

/**
 * Jetpack Compose Alpha Painter
 *
 * @author Martin Zeitler
 */
class AlphaPainter(intrinsicSize: Size) : BasePainter(intrinsicSize) {

    private var value: Float = 1.0F

    /**
     * Implementation of drawing logic for instances of [Painter].
     * This is invoked internally within [draw] after the positioning and configuring the [Painter].
     */
    override fun DrawScope.onDraw() {
        setCanvas(drawContext, density)
        drawRect(
            size = size,
            brush = Brush.linearGradient(
                colors = listOf(Color.Transparent, Color.Black)
            )
        ).also {

            /* Border */
            drawRect(
                size = size,
                color = Color(borderStrokeColor),
                topLeft = Offset(outerRect.left, outerRect.top),
                style = Stroke(width = borderStrokeWidth,
                    pathEffect = PathEffect.cornerPathEffect(borderCornerRadius)
                )
            )

            /* Horizontal Tracker */
            val p: Point = alphaToPoint(value)

            /* Outer rectangle (dark) */
            drawRoundRect(
                color = Color(trackerStrokeColorOuter),
                size = Size(alphaTrackerWidth, innerRect.height()),
                topLeft = Offset(p.x - (alphaTrackerWidth / 2), innerRect.top),
                style = Stroke(width = trackerStrokeWidth,
                    pathEffect = PathEffect.cornerPathEffect(trackerCornerRadius)
                )
            )

            /* Inner rectangle (light) */
            drawRoundRect(
                color = Color(trackerStrokeColorInner),
                size = Size(alphaTrackerWidth - 2, innerRect.height() - 2),
                topLeft = Offset(p.x - (alphaTrackerWidth / 2) + 1, innerRect.top + 1),
                style = Stroke(width = trackerStrokeWidth,
                    pathEffect = PathEffect.cornerPathEffect(trackerCornerRadius)
                )
            )
        }
    }

    fun setAlphaByColor(color: Int) {
        this.value = Color(color).alpha
    }
}
