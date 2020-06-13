package com.ww.colors.weight

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class VerticalTextView : AppCompatTextView {
    private var mIsVerticalMode = true
    private var mLineCount:Int=0
    private lateinit var mLineWidths
            : FloatArray
    private lateinit var mLineBreakIndex
            : IntArray

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        init()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {}

    @SuppressLint("DrawAllocation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mIsVerticalMode) {
            val widthMode = MeasureSpec.getMode(widthMeasureSpec)
            val heightMode = MeasureSpec.getMode(heightMeasureSpec)
            val widthSize = MeasureSpec.getSize(widthMeasureSpec)
            val heightSize = MeasureSpec.getSize(heightMeasureSpec)
            var width = paddingLeft + paddingRight.toFloat()
            var height = paddingTop + paddingBottom.toFloat()
            val chars = text.toString().toCharArray()
            val paint: Paint = paint
            val fontMetricsInt = paint.fontMetricsInt
            val lineMaxBottom =
                ((if (heightMode == MeasureSpec.UNSPECIFIED) Int.MAX_VALUE else heightSize)
                        - paddingBottom)
            var currentLineHeight = paddingTop.toFloat()
            var lineMaxHeight = currentLineHeight
            var lineIndex = 0
            mLineCount = 0
            mLineWidths =
                FloatArray(chars.size + 1) // 加1是为了处理高度不够放下一个字的情况,needBreakLine会一直为true直到最后一个字
            mLineBreakIndex = IntArray(chars.size + 1)
            // 从右向左,从上向下布局
            for (i in chars.indices) {
                val c = chars[i]
                // rotate
//            boolean needRotate = !Caches.isCJK(c);
                val needRotate = !isCJKCharacter(c)
                // char height
                var charHeight: Float
                var charWidth: Float
                if (needRotate) {
                    charWidth = fontMetricsInt.descent - fontMetricsInt.ascent.toFloat()
                    charHeight = paint.measureText(chars, i, 1)
                } else {
                    charWidth = paint.measureText(chars, i, 1)
                    charHeight = fontMetricsInt.descent - fontMetricsInt.ascent.toFloat()
                }

                // is need break line
                val needBreakLine = (currentLineHeight + charHeight > lineMaxBottom
                        && i > 0) // i > 0 是因为即使在第一列高度不够,也不用换下一列
                if (needBreakLine) {
                    // break line
                    if (lineMaxHeight < currentLineHeight) {
                        lineMaxHeight = currentLineHeight
                    }
                    mLineBreakIndex[lineIndex] = i - 1
                    width += mLineWidths[lineIndex]
                    lineIndex++
                    // reset
                    currentLineHeight = charHeight
                } else {
                    currentLineHeight += charHeight
                    if (lineMaxHeight < currentLineHeight) {
                        lineMaxHeight = currentLineHeight
                    }
                }
                if (mLineWidths[lineIndex] < charWidth) {
                    mLineWidths[lineIndex] = charWidth
                }
                // last column width
                if (i == chars.size - 1) {
                    width += mLineWidths[lineIndex]
                    height = lineMaxHeight + paddingBottom
                }
            }
            if (chars.size > 0) {
                mLineCount = lineIndex + 1
                mLineBreakIndex[lineIndex] = chars.size - 1
            }

            // 计算 lineSpacing
            if (mLineCount > 1) {
                val lineSpacingCount = mLineCount - 1
                for (i in 0 until lineSpacingCount) {
                    width += mLineWidths[i] * (lineSpacingMultiplier - 1) + lineSpacingExtra
                }
            }
            if (heightMode == MeasureSpec.EXACTLY) {
                height = heightSize.toFloat()
            } else if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize.toFloat())
            }
            if (widthMode == MeasureSpec.EXACTLY) {
                width = widthSize.toFloat()
            } else if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, widthSize.toFloat())
            }
            setMeasuredDimension(width.toInt(), height.toInt())
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onDraw(canvas: Canvas) {
        if (!mIsVerticalMode) {
            super.onDraw(canvas)
        } else {
            if (mLineCount == 0) {
                return
            }
            val paint = paint
            paint.color = currentTextColor
            paint.drawableState = drawableState
            val fontMetricsInt = paint.fontMetricsInt
            val chars = text.toString().toCharArray()
            canvas.save()
            var curLine = 0
            var curLineX = width - paddingRight - mLineWidths[curLine]
            var curX = curLineX
            var curY = paddingTop.toFloat()
            for (i in chars.indices) {
                val c = chars[i]
                //            boolean needRotate = !Caches.isCJK(c);
                val needRotate = !isCJKCharacter(c)
                val saveCount = canvas.save()
                if (needRotate) {
                    canvas.rotate(90f, curX, curY)
                }
                // draw
                val textX = curX
                val textBaseline =
                    if (needRotate) curY - (mLineWidths[curLine] - (fontMetricsInt.bottom - fontMetricsInt.top)) / 2 - fontMetricsInt.descent else curY - fontMetricsInt.ascent
                canvas.drawText(chars, i, 1, textX, textBaseline, paint)
                canvas.restoreToCount(saveCount)

                // if break line
                val hasNextChar = i + 1 < chars.size
                if (hasNextChar) {
//                boolean breakLine = needBreakLine(i, mLineCharsCount, curLine);
                    val nextCharBreakLine = i + 1 > mLineBreakIndex[curLine]
                    if (nextCharBreakLine && curLine + 1 < mLineWidths.size) {
                        // new line
                        curLine++
                        curLineX -= mLineWidths[curLine] * lineSpacingMultiplier + lineSpacingExtra
                        curX = curLineX
                        curY = paddingTop.toFloat()
                    } else {
                        // move to next char
                        curY += if (needRotate) {
                            paint.measureText(chars, i, 1)
                        } else {
                            fontMetricsInt.descent - fontMetricsInt.ascent.toFloat()
                        }
                    }
                }
            }
            canvas.restore()
        }
    }

    var isVerticalMode: Boolean
        get() = mIsVerticalMode
        set(verticalMode) {
            mIsVerticalMode = verticalMode
            requestLayout()
        }

    companion object {
        // This method is copied from moai.ik.helper.CharacterHelper.isCJKCharacter(char input)
        private fun isCJKCharacter(input: Char): Boolean {
            val ub = Character.UnicodeBlock.of(input)
            return if (ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A //全角数字字符和日韩字符
                || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS //韩文字符集
                || ub === Character.UnicodeBlock.HANGUL_SYLLABLES || ub === Character.UnicodeBlock.HANGUL_JAMO || ub === Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO //日文字符集
                || ub === Character.UnicodeBlock.HIRAGANA //平假名
                || ub === Character.UnicodeBlock.KATAKANA //片假名
                || ub === Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS
            ) {
                true
            } else {
                false
            }
            //其他的CJK标点符号，可以不做处理
            //|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
            //|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
        }
    }
}