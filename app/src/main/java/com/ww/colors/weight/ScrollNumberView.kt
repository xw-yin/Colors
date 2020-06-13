package com.ww.colors.weight

import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.util.AttributeSet
import android.view.animation.Interpolator
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import com.ww.colors.R

class ScrollNumberView : AppCompatTextView{

    private var currentNumberOfFloat = 0f
    private var currentNumberOfInt = 0
    private var intNum = 0
    private var floatNum = 0f
    private var objectAnimator = ObjectAnimator()
    private var duration: Long = 0
    private var delay = 0
    private var format: String? = null
    private var interpolator: Interpolator? = null
    constructor(context: Context,attributeSet: AttributeSet):super(context,attributeSet){
        val typedArray: TypedArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.ScrollNumberView)
        intNum = typedArray.getInt(R.styleable.ScrollNumberView_intNum, 0)
        floatNum = typedArray.getFloat(R.styleable.ScrollNumberView_floatNum, 0f)
        delay = typedArray.getInt(R.styleable.ScrollNumberView_delay, 0)
        duration = typedArray.getInt(R.styleable.ScrollNumberView_duration, 1000).toLong()
        typedArray.recycle()
    }

    fun setIntNum(intNum: Int) {
        this.intNum = intNum
        setNumberSetOfInt(intNum).startAnim()
    }

    fun setNumberSetOfInt(vararg numbers: Int): ScrollNumberView {
        var tempNumbers = numbers
        if (numbers.size == 1) {
            tempNumbers = intArrayOf(currentNumberOfInt, numbers[0])
        }
        objectAnimator = ObjectAnimator.ofInt(this, "currentNumberOfInt", *tempNumbers)
        return this
    }


    fun setNumberSetOfFloat(vararg numbers: Float): ScrollNumberView? {
        var tempNumbers = numbers
        if (numbers.size == 1) {
            tempNumbers = floatArrayOf(currentNumberOfFloat, numbers[0])
        }
        objectAnimator = ObjectAnimator.ofFloat(this, "currentNumberOfFloat", *tempNumbers)
        return this
    }

    fun setCurrentNumberOfInt(number: Int) {
        currentNumberOfInt = number
        if (format == null) {
            this.text = number.toString() + ""
        } else {
            this.text = String.format(format!!, number)
        }
    }

    fun setCurrentNumberOfFloat(number: Float) {
        currentNumberOfFloat = number
        if (format == null) {
            this.text = number.toString() + ""
        } else {
            this.text = String.format(format!!, number)
        }
    }


    fun setDuration(duration: Long): ScrollNumberView? {
        this.duration = duration
        return this
    }


    fun setDelay(delay: Int): ScrollNumberView? {
        this.delay = delay
        return this
    }


    fun getDelay(): Int {
        return delay
    }


    fun setFormat(format: String?): ScrollNumberView? {
        this.format = format
        return this
    }


    fun getFormat(): String? {
        return format
    }


    fun setInterpolator(interpolator: Interpolator?): ScrollNumberView? {
        this.interpolator = interpolator
        return this
    }


    fun getInterpolator(): Interpolator? {
        return interpolator
    }


    fun isAnimRunning(): Boolean {
        return objectAnimator.isRunning
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun isAnimPaused(): Boolean {
        return objectAnimator.isPaused
    }


    fun isAnimStarted(): Boolean {
        return objectAnimator.isStarted
    }


    fun startAnim() {
        objectAnimator.setDuration(duration).startDelay = delay.toLong()
        objectAnimator.interpolator = interpolator
        objectAnimator.start()
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun pauseAnim() {
        objectAnimator.pause()
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun resumeAnim() {
        objectAnimator.resume()
    }
}