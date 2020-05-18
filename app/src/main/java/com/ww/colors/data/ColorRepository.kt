package com.ww.colors.data

class ColorRepository private constructor(private val colorDao: ColorDao) {
    fun getColors() = colorDao.getColors()
    fun getColor(colorId: String) = colorDao.getColor(colorId)

    companion object {
        @Volatile private var instance: ColorRepository? = null
        fun getInstance(colorDao: ColorDao) =
            instance ?: synchronized(this) {
                instance ?: ColorRepository(colorDao).also { instance = it }
            }
    }
}