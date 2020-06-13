package com.ww.colors.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ww.colors.data.Color
import com.ww.colors.data.ColorRepository


class ColorDetailViewModel internal constructor(colorRepository: ColorRepository,colorId:String):
    ViewModel() {
    val color:LiveData<Color> = colorRepository.getColor(colorId)
}