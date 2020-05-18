package com.ww.colors.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ww.colors.data.Color
import com.ww.colors.data.ColorRepository

class ColorListViewModel internal constructor(
    colorRepository: ColorRepository
) : ViewModel() {
    val colors:LiveData<List<Color>> =colorRepository.getColors()
}