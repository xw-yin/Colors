package com.ww.colors.converter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.databinding.BindingConversion
    @BindingConversion
    fun converterColorStringToDrawable(colorString: String): ColorDrawable {
            var colorInt = Color.parseColor(colorString)
            return ColorDrawable(colorInt)
}