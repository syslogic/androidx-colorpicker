package io.syslogic.colorpicker.compose

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import kotlin.properties.Delegates

/**
 * Jetpack Compose Color Painter
 *
 * @author Martin Zeitler
 */
class ColorPainter(intrinsicSize: Size) : BasePainter(intrinsicSize) {

    private var borderRadius by Delegates.notNull<Float>()
    private var value: Int = Color.Black.hashCode()

    /**
     * Implementation of drawing logic for instances of [Painter]. This is invoked
     * internally within [draw] after the positioning and configuring the [Painter]
     */
    override fun DrawScope.onDraw() {
        borderRadius = 2 * density
        setCanvas(drawContext)
        drawRect(
            size = size,
            color = Color(value)
        ).also {

            /* Border */
            val offset = Offset(bounds.left.toFloat(), bounds.top.toFloat())
            drawRect(
                color = Color(borderColor),
                size = size,
                topLeft = offset,
                style = Stroke(width = 4f,
                    pathEffect = PathEffect.cornerPathEffect(borderRadius)
                )
            )
        }
    }

    fun setValue(value: Int) {
        this.value = value
    }
}
