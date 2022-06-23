package io.syslogic.colorpicker.compose

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter

class SatPainter(override val intrinsicSize: Size) : Painter() {

    private var color: Int = Color.Black.hashCode()

    /**
     * Implementation of drawing logic for instances of [Painter]. This is invoked
     * internally within [draw] after the positioning and configuring the [Painter]
     */
    override fun DrawScope.onDraw() {
        drawRect(
            size = size,
            brush = Brush.verticalGradient(
                colors = getValues()
            )
        )
    }

    private fun getValues(): List<Color> {
        val list: MutableList<Color> = MutableList(361) { Color.Black }
        val arr = IntArray(361)
        var i = arr.size - 1
        var count = 0
        while (i >= 0) {
            list[count] = Color(android.graphics.Color.HSVToColor(floatArrayOf(i.toFloat(), 1f, 1f)))
            i--
            count++
        }
        return list
    }

    fun setColor(value: Int) {
        this.color = value
    }
}
