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
class ColorPainter(intrinsicSize: Size) : BasePainter(intrinsicSize) {

    private var value: Int = Color.Black.hashCode()

    /**
     * Implementation of drawing logic for instances of [Painter]. This is invoked
     * internally within [draw] after the positioning and configuring the [Painter]
     */
    override fun DrawScope.onDraw() {
        setCanvas(drawContext)
        drawRect(
            size = size,
            color = Color(value)
        )
    }

    fun setValue(value: Int) {
        this.value = value
    }
}
