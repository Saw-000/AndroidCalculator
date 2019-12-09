package com.all_man.androidcalculator

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView

class AutoFontFitTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    override fun onLayout(changed: Boolean, left:Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        resize()
    }

    private fun resize() {

        val viewHeight = this.height
        val viewWidth = this.width
        var textSize = this.textSize

        var textHeight = calcTextHeight(textSize,viewWidth)

        while (viewHeight <= textHeight) {
            if (1f >= textSize) {
                textSize = 1f
                break
            }

            textSize -= 1f
            textHeight = calcTextHeight(textSize,viewWidth)
        }

        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

    private fun calcTextHeight(textSize:Float,viewWidth:Int): Float {
        val paint = Paint()
        paint.textSize = textSize
        val textHeightPerRow = paint.getFontMetrics(null)
        val textPerLines = this.text.toString().split("\n")

        var rowCount = 0

        for (i in 0..textPerLines.size - 1) {
            val textWidth = paint.measureText(textPerLines[i])
            rowCount += Math.ceil(textWidth.toDouble() / viewWidth.toDouble()).toInt()
            if (i != textPerLines.size - 1) {
                rowCount += 1
            }
        }
        return textHeightPerRow * rowCount
    }
}