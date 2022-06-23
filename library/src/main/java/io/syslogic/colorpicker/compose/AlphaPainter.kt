package io.syslogic.colorpicker.compose

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter

class AlphaPainter(override val intrinsicSize: Size) : Painter() {

    /**
     * Implementation of drawing logic for instances of [Painter]. This is invoked
     * internally within [draw] after the positioning and configuring the [Painter]
     */
    override fun DrawScope.onDraw() {
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(Color.Transparent, Color.Black)
            ),
            size = size
        )
    }
}
