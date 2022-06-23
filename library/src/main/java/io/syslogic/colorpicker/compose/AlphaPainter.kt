package io.syslogic.colorpicker.compose

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter

class AlphaPainter(override val intrinsicSize: Size) : Painter() {

    private var alpha: Float = 1F

    /**
     * Implementation of drawing logic for instances of [Painter]. This is invoked
     * internally within [draw] after the positioning and configuring the [Painter]
     */
    override fun DrawScope.onDraw() {
        drawRect(
            size = size,
            brush = Brush.linearGradient(
                colors = listOf(Color.Transparent, Color.Black)
            )
        )
    }

    fun setAlpha(color: Int) {
        this.alpha = Color(color).alpha
    }
}
