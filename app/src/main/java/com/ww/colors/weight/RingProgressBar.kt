package com.ww.colors.weight

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import com.ww.colors.R

class RingProgressBar :
    BaseProgressBar{
    private var mReachedBarWidth = 0f
    private var mUnreachedBarWidth = 0f
    private var mStartAngle = 0f
    constructor(context: Context?):this(context,null)
    constructor(context: Context?,attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        val default_reached_bar_width = dp2px(1.5f)
        val default_unreached_bar_width = dp2px(1.0f)

        val typedArray:TypedArray? =
            context?.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, defStyleAttr, 0)!!
        mReachedBarWidth = typedArray?.getDimension(
            R.styleable.CustomProgressBar_reached_bar_width,
            default_reached_bar_width
        )!!
        mUnreachedBarWidth = typedArray.getDimension(
            R.styleable.CustomProgressBar_unreached_bar_width,
            default_unreached_bar_width
        )
        mStartAngle = typedArray.getFloat(R.styleable.CustomProgressBar_start_angle, 0f)
        typedArray.recycle()
        setupPaints()
    }
    override fun setupPaints() {
        super.setupPaints()
        mReachedBarPaint.style = Paint.Style.STROKE
        mReachedBarPaint.strokeWidth = mReachedBarWidth
        if (mIfDrawUnreachedBar) {
            mUnReachedBarPaint.setStyle(Paint.Style.STROKE)
            mUnReachedBarPaint.setStrokeWidth(mUnreachedBarWidth)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            MeasureSpec.getSize(widthMeasureSpec),
            MeasureSpec.getSize(heightMeasureSpec)
        )
    }
    override fun onDraw(canvas: Canvas) {
        if (mIfDrawText) {
            setupRectFWithText()
            canvas.drawText(mDrawText, mDrawTextX, mDrawTextY, mTextPaint)
        } else {
            setupRectFWithoutText()
        }
        if (mIfDrawUnreachedBar) {
            canvas.drawArc(mUnReachedRectF, 0f, 360f, false, mUnReachedBarPaint)
        }
        canvas.drawArc(
            mReachedRectF,
            mStartAngle,
            mShownProgress * 360 / mMaxProgress.toFloat(),
            false,
            mReachedBarPaint
        )
    }

    protected fun setupRectFWithoutText() {
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom
        val widthWithoutPadding = width - paddingLeft - paddingRight
        val heightWithoutPadding = height - paddingTop - paddingBottom
        val diameter = Math.min(widthWithoutPadding, heightWithoutPadding)
        mReachedRectF.left = paddingLeft + mReachedBarWidth / 2.0f
        mReachedRectF.top = paddingTop + mReachedBarWidth / 2.0f
        mReachedRectF.right = paddingLeft + diameter - mReachedBarWidth / 2.0f
        mReachedRectF.bottom = paddingTop + diameter - mReachedBarWidth / 2.0f
        if (mIfDrawUnreachedBar) {
            mUnReachedRectF.left = paddingLeft + mUnreachedBarWidth / 2.0f
            mUnReachedRectF.top = paddingTop + mUnreachedBarWidth / 2.0f
            mUnReachedRectF.right = paddingLeft + diameter - mUnreachedBarWidth / 2.0f
            mUnReachedRectF.bottom = paddingTop + diameter - mUnreachedBarWidth / 2.0f
        }
    }

    protected fun setupRectFWithText() {
        setupRectFWithoutText()
        mDrawText = String.format("%d", mShownProgress * 100 / mMaxProgress)
        mDrawText = mPrefix + mDrawText + mSuffix
        val DrawTextWidth = mTextPaint.measureText(mDrawText)
        mDrawTextX = (mReachedRectF.right + mReachedRectF.left) / 2.0f - DrawTextWidth / 2.0f
        mDrawTextY =
            (mReachedRectF.bottom + mReachedRectF.top) / 2.0f - (mTextPaint.descent() + mTextPaint.ascent()) / 2.0f
    }

    fun setReachedBarWidth(width: Float) {
        mReachedBarWidth = width
        mReachedBarPaint.strokeWidth = width
    }

    fun setUnreachedBarWidth(width: Float) {
        if (mIfDrawUnreachedBar) {
            mUnreachedBarWidth = width
            mUnReachedBarPaint.setStrokeWidth(width)
        }
    }

    fun getReachedBarWidth(): Float {
        return mReachedBarWidth
    }

    fun getUnreachedBarWidth(): Float {
        return mUnreachedBarWidth
    }

    fun getReachedBarRadius(): Float {
        return (mReachedRectF.right - mReachedRectF.left + mReachedBarWidth) / 2.0f
    }

    fun getUnreachedBarRadius(): Float {
        return (mUnReachedRectF.right - mUnReachedRectF.left + mUnreachedBarWidth) / 2.0f
    }

}