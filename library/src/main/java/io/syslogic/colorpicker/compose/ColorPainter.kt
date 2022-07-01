package io.syslogic.colorpicker.compose

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter

/**
 * Jetpack Compose Color Painter
 *
 * @author Martin Zeitler
 */
class ColorPainter(override val intrinsicSize: Size) : Painter() {

    private var color: Int = Color.Black.hashCode()

    /**
     * Implementation of drawing logic for instances of [Painter]. This is invoked
     * internally within [draw] after the positioning and configuring the [Painter]
     */
    override fun DrawScope.onDraw() {
        drawRect(
            size = size,
            color = Color(color)
        )
    }

    fun setColor(value: Int) {
        this.color = value
    }
}
