package com.ww.colors.weight

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import com.ww.colors.R

open class BaseProgressBar :
    View {
    protected val DEFAULT_REACHED_COLOR = 0xFF3F51B5
    protected val DEFAULT_UNREACHED_COLOR = 0xFF3F51B5
    protected val DEFAULT_TEXT_COLOR = 0xFF3F51B5

    protected lateinit var mUnReachedBarPaint: Paint
    protected lateinit var mReachedBarPaint: Paint
    protected lateinit var mTextPaint: Paint

    protected var mIfDrawUnreachedBar = true
    protected var mIfDrawReachedBar = true

    protected var mReachedBarColor = 0
    protected var mUnReachedBarColor = 0

    protected var mReachedRectF = RectF()
    protected var mUnReachedRectF = RectF()

    protected var mMaxProgress = 100
    protected var mActualProgress = 0
    protected var mShownProgress = 0

    protected var mIfDrawText = false
    protected lateinit var mDrawText: String
    protected var mTextSize: Float = 0.0f
    protected var mTextColor = 0
    protected var mTextOffset: Float = 0.0f

    protected var mDrawTextX: Float = 0.0f
    protected var mDrawTextY: Float = 0.0f

    protected var mSuffix: String? = ""
    protected var mPrefix: String? = ""

    protected var mProgressDuration = 0
    protected var mProgressAnimator=ValueAnimator()
    constructor(context: Context?) : this(
        context,null
    )
    constructor(context: Context?, attrs: AttributeSet?) : this(
        context,
        attrs,
        0
    )
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val default_text_size = sp2px(10f)
        val default_progress_text_offset = dp2px(3f)
        val typedArray: TypedArray? =
            context?.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, defStyleAttr, 0)
        mReachedBarColor = typedArray?.getColor(
            R.styleable.CustomProgressBar_reached_bar_color,
            DEFAULT_REACHED_COLOR.toInt()
        )!!
        mIfDrawReachedBar =
            typedArray.getBoolean(R.styleable.CustomProgressBar_unreached_bar_visibility, true)
        mUnReachedBarColor = typedArray.getColor(
            R.styleable.CustomProgressBar_unreached_bar_color,
            DEFAULT_UNREACHED_COLOR.toInt()
        )

        mIfDrawText = typedArray.getBoolean(R.styleable.CustomProgressBar_text_visibility, true)
        if (mIfDrawText) {
            mTextOffset = typedArray.getDimension(
                R.styleable.CustomProgressBar_text_offset,
                default_progress_text_offset
            )
            mTextColor = typedArray.getColor(
                R.styleable.CustomProgressBar_text_color,
                DEFAULT_TEXT_COLOR.toInt()
            )
            mTextSize =
                typedArray.getDimension(R.styleable.CustomProgressBar_text_size, default_text_size)
            mPrefix =
                if (typedArray.hasValue(R.styleable.CustomProgressBar_prefix)) typedArray.getString(
                    R.styleable.CustomProgressBar_prefix
                ) else ""
            mSuffix =
                if (typedArray.hasValue(R.styleable.CustomProgressBar_suffix)) typedArray.getString(
                    R.styleable.CustomProgressBar_suffix
                ) else ""
        }
        mMaxProgress = typedArray.getInt(R.styleable.CustomProgressBar_progressMax, 100)
        mActualProgress = typedArray.getInt(R.styleable.CustomProgressBar_progressNow, 0)
        mShownProgress = mActualProgress
        if (mShownProgress > mMaxProgress) {
            mActualProgress = mMaxProgress
            mShownProgress = mMaxProgress
        }
        mProgressDuration = typedArray.getInt(R.styleable.CustomProgressBar_duration, 1500)
        typedArray.recycle()
    }
    open fun setupPaints(){
        mReachedBarPaint= Paint(Paint.ANTI_ALIAS_FLAG)
        mReachedBarPaint.setColor(mReachedBarColor)
        if (mIfDrawUnreachedBar){
            mUnReachedBarPaint=Paint(Paint.ANTI_ALIAS_FLAG)
            mUnReachedBarPaint.setColor(mUnReachedBarColor)
        }
        if (mIfDrawText){
            mTextPaint=Paint(Paint.ANTI_ALIAS_FLAG)
            mTextPaint.setColor(mTextColor)
            mTextPaint.textSize=mTextSize
            mTextPaint.setTypeface(Typeface.SANS_SERIF)
        }
    }

    open fun getTextColor():Int = mTextColor
    open fun getProgressTextSize():Float = mTextSize
    open fun getUnreachedBarColor():Int = mUnReachedBarColor
    open fun getReachedBarColor():Int = mReachedBarColor
    open fun getActualProgress():Int = mActualProgress
    open fun getShownProgress():Int =mShownProgress
    open fun getMax():Int = mMaxProgress
    open fun setProgressTextVisibility(visibility:Boolean){
        mIfDrawText =visibility
        invalidate()
    }
    open fun getProgressTextVisibility():Boolean = mIfDrawText
    open fun setProgressTextSize(textsize:Float){
        mTextSize=textsize
        mTextPaint.textSize=mTextSize
        invalidate()
    }
    open fun setProgressTextColor(textColor:Int){
        mTextColor=textColor
        mTextPaint.setColor(mTextColor)
        invalidate()
    }
    open fun setUnreachedBarColor(barColor:Int){
        this.mUnReachedBarColor=barColor
        mUnReachedBarPaint.setColor(mUnReachedBarColor)
        invalidate()
    }
    open fun setReachedBarColor(progressColor:Int){
        this.mReachedBarColor=progressColor
        mReachedBarPaint.setColor(mReachedBarColor)
        invalidate()
    }
    open fun setMax(maxProgress:Int){
        if(maxProgress>0){
            this.mMaxProgress=maxProgress
            invalidate()
        }
    }
    open fun setSuffix(suffix:String){
            mSuffix=suffix
    }
    open fun getSuffix()=mSuffix
    open fun setPrefix(prefix:String){
    if(mSuffix==null){
        mPrefix=""
    }else{
        mPrefix=prefix
    }
}
    open fun getPrefix()=mPrefix
    open fun addProgressBy(by:Int){
        if(by>0){
            setProgress(mActualProgress+by)
        }
    }
    open fun setProgress(progress:Int){
        if(progress<=mMaxProgress&&progress>=0){
            mActualProgress=progress
        }else if(progress>mMaxProgress){
            mActualProgress=mMaxProgress
        }
        seartAnim()
    }
    open fun resetProgress(){
        if(mProgressAnimator.isStarted()){
            mProgressAnimator.cancel()
        }
        mActualProgress=0
        mShownProgress=0
        invalidate()
    }
    fun seartAnim(){
        if(mProgressAnimator.isStarted()){
            mProgressAnimator.cancel()
        }
        mProgressAnimator= ValueAnimator.ofInt(mShownProgress,mActualProgress)
        mProgressAnimator.setDuration(mProgressDuration*Math.abs(mActualProgress-mShownProgress)/mMaxProgress.toLong())
        mProgressAnimator.addUpdateListener {
            mShownProgress= it.animatedValue as Int
            invalidate()
        }
        mProgressAnimator.start()
    }
    fun dp2px(dp: Float):
            Float {
        val scale = resources.displayMetrics.density
        return dp * scale + 0.5f
    }

    fun sp2px(sp: Float): Float {
        val scale = resources.displayMetrics.scaledDensity
        return sp * scale
    }
}