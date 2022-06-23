package io.syslogic.colorpicker.compose

import android.graphics.*

import android.graphics.Color.HSVToColor
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter

class HuePainter(override val intrinsicSize: Size) : Painter() {

    private lateinit var mRect: Rect
    private var mHue: Float = 360f

    /**
     * Implementation of drawing logic for instances of [Painter].
     * This is invoked internally within [draw] after the positioning and configuring the [Painter].
     */
    override fun DrawScope.onDraw() {

        val paint = Paint()
        val canvas = drawContext.canvas.nativeCanvas
        mRect = canvas.clipBounds

        val mValueShader = LinearGradientShader(
            from = Offset(mRect.left.toFloat(), mRect.bottom.toFloat()),
            to = Offset(mRect.left.toFloat(), mRect.top.toFloat()),
            tileMode = androidx.compose.ui.graphics.TileMode.Clamp,
            colors = listOf(Color.Black, Color.White)
        )

        val mSatShader = LinearGradientShader(
            from = Offset(mRect.right.toFloat(), mRect.top.toFloat()),
            to = Offset(mRect.left.toFloat(), mRect.top.toFloat()),
            tileMode = androidx.compose.ui.graphics.TileMode.Clamp,
            colors = getValues()
        )

        paint.shader = ComposeShader(mValueShader, mSatShader, PorterDuff.Mode.MULTIPLY)
        canvas.drawRect(mRect, paint)
    }

    private fun getValues(): List<Color> {
        val list: MutableList<Color> = MutableList(361) { Color.Black }
        val arr = IntArray(361)
        var i = arr.size - 1
        var count = 0
        while (i >= 0) {
            list[count] = Color(HSVToColor(floatArrayOf(i.toFloat(), 1f, 1f)))
            i--
            count++
        }
        return list
    }

    fun setColor(value: Int) {
        this.mHue = value.toFloat()
    }
}
