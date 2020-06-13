package com.ww.colors.weight

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.util.AttributeSet
import com.ww.colors.R

class VerticalProgressBar :BaseProgressBar{
    private var mReachedBarWidth = 0f
    private var mUnreachedBarWidth = 0f
    constructor(context: Context?):this(context,null)
    constructor(context: Context?, attributeSet: AttributeSet?):this(context,attributeSet,0)
    constructor(context: Context?, attributeSet: AttributeSet?, defStyleAttr: Int):super(context,attributeSet,defStyleAttr){
        val default_reached_bar_width = dp2px(1.5f)
        val default_unreached_bar_width = dp2px(1.0f)

        val typedArray: TypedArray? =
            context?.obtainStyledAttributes(attributeSet, R.styleable.CustomProgressBar, defStyleAttr, 0)

        mReachedBarWidth = typedArray?.getDimension(
            R.styleable.CustomProgressBar_reached_bar_width,
            default_reached_bar_width
        )!!

        mUnreachedBarWidth = typedArray.getDimension(
            R.styleable.CustomProgressBar_unreached_bar_width,
            default_unreached_bar_width
        )


        typedArray.recycle()
        setupPaints()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width: Int
        val width_mode = MeasureSpec.getMode(widthMeasureSpec)
        width = if (width_mode == MeasureSpec.EXACTLY) {
            MeasureSpec.getSize(widthMeasureSpec)
        } else {
            getMinWidth() + paddingLeft + paddingRight
        }
        setMeasuredDimension(width, MeasureSpec.getSize(heightMeasureSpec))
    }

    private fun getMinWidth(): Int {
        var result = Math.max(mReachedBarWidth.toInt(), mUnreachedBarWidth.toInt())
        if (mIfDrawText) {
            result = Math.max(
                result,
                mTextPaint.measureText(mPrefix + "100" + mSuffix).toInt()
            )
        }
        return result
    }

    override fun onDraw(canvas: Canvas) {
        if (mIfDrawText) {
            setupRectFWithText()
            canvas.drawText(mDrawText, mDrawTextX, mDrawTextY, mTextPaint)
        } else {
            setupRectFWithoutText()
        }
        if (mIfDrawReachedBar) {
            canvas.drawRect(mReachedRectF, mReachedBarPaint)
        }
        if (mIfDrawUnreachedBar && mUnReachedRectF.top < height - paddingBottom) {
            canvas.drawRect(mUnReachedRectF, mUnReachedBarPaint)
        }
    }

    protected fun setupRectFWithoutText() {
        mReachedRectF.left = paddingLeft.toFloat()
        mReachedRectF.top = paddingTop.toFloat()
        mReachedRectF.right = mReachedRectF.left + mReachedBarWidth
        mReachedRectF.bottom =
            paddingTop + (height - paddingTop - paddingBottom) / (mMaxProgress * 1.0f) * mShownProgress
        if (mIfDrawUnreachedBar) {
            mUnReachedRectF.left = paddingLeft.toFloat()
            mUnReachedRectF.top = mReachedRectF.bottom
            mUnReachedRectF.right = mUnReachedRectF.left + mUnreachedBarWidth
            mUnReachedRectF.bottom = (height - paddingBottom).toFloat()
        }
    }

    protected fun setupRectFWithText() {
        mDrawText = String.format("%3d", mShownProgress * 100 / mMaxProgress)
        mDrawText = mPrefix + mDrawText + mSuffix
        val drawTextWidth = mTextPaint.measureText(mPrefix + "100" + mSuffix)
        mDrawTextX =
            paddingLeft + (width - paddingLeft - paddingRight) / 2.0f - drawTextWidth / 2.0f
        if (mShownProgress == 0) {
            mIfDrawReachedBar = false
            mDrawTextY =
                paddingTop + mTextSize / 2.0f - (mTextPaint.descent() + mTextPaint.ascent()) / 2.0f
        } else {
            mIfDrawReachedBar = true
            mReachedRectF.left =
                paddingLeft + (width - paddingLeft - paddingRight) / 2.0f - mReachedBarWidth / 2.0f
            mReachedRectF.top = paddingTop.toFloat()
            mReachedRectF.right = mReachedRectF.left + mReachedBarWidth
            mReachedRectF.bottom =
                paddingTop + (height - paddingTop - paddingBottom) / (mMaxProgress * 1.0f) * mShownProgress - mTextOffset
            mDrawTextY =
                mReachedRectF.bottom + mTextOffset + mTextSize / 2.0f - (mTextPaint.descent() + mTextPaint.ascent()) / 2.0f
        }
        if (mDrawTextY + mTextPaint.descent() > height - paddingBottom) {
            mDrawTextY = height - paddingBottom - mTextPaint.descent()
            mReachedRectF.bottom =
                mDrawTextY - mTextOffset - mTextSize / 2.0f + (mTextPaint.descent() + mTextPaint.ascent()) / 2.0f
        }
        if (mIfDrawUnreachedBar) {
            mUnReachedRectF.left =
                paddingLeft + (width - paddingLeft - paddingRight) / 2.0f - mUnreachedBarWidth / 2.0f
            mUnReachedRectF.top = mDrawTextY + mTextPaint.descent() + mTextOffset
            mUnReachedRectF.right = mUnReachedRectF.left + mUnreachedBarWidth
            mUnReachedRectF.bottom = (height - paddingBottom).toFloat()
        }
    }

    fun setReachedBarWidth(width: Float) {
        mReachedBarWidth = width
    }

    fun setUnreachedBarWidth(width: Float) {
        mUnreachedBarWidth = width
    }

    fun getReachedBarWidth(): Float {
        return mReachedBarWidth
    }

    fun getUnreachedBarWidth(): Float {
        return mUnreachedBarWidth
    }
}