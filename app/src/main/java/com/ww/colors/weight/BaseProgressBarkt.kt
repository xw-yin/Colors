package com.ww.colors.weight

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

class BaseProgressBarkt(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr) {
    protected val DEFAULT_REACHED_COLOR="0xFF3F51B5"
    protected val DEFAULT_UNREACHED_COLOR="0xFF3F51B5"
    protected val DEFAULT_TEXT_COLOR="0xFF3F51B5"

    protected lateinit var  mUnReachedBarPaint:Paint
    protected lateinit var  mReachedBarPaint:Paint
    protected lateinit var  mTextViewPaint:Paint

}