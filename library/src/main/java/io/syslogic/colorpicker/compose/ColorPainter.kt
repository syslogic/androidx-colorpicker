package io.syslogic.colorpicker.compose

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter

/**
 * Jetpack Compose Color Painter
 *
 * @author Martin Zeitler
 */
class ColorPainter(intrinsicSize: Size) : BasePainter(intrinsicSize) {

    private var value: Int = Color.Black.hashCode()

    /**
     * Implementation of drawing logic for instances of [Painter]. This is invoked
     * internally within [draw] after the positioning and configuring the [Painter]
     */
    override fun DrawScope.onDraw() {
        setCanvas(drawContext, density)
        drawRect(
            size = size,
            color = Color(value)
        ).also {

            /* Border */
            drawRect(
                color = Color(borderStrokeColor),
                size = size,
                topLeft = Offset(bounds.left.toFloat(), bounds.top.toFloat()),
                style = Stroke(width = borderStrokeWidth,
                    pathEffect = PathEffect.cornerPathEffect(borderCornerRadius)
                )
            )
        }
    }

    fun setValue(value: Int) {
        this.value = value
    }
}
