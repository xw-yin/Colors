package com.ww.colors.utilities

import android.content.Context
import androidx.fragment.app.Fragment
import com.ww.colors.data.AppDatabase
import com.ww.colors.data.ColorRepository
import com.ww.colors.viewmodels.ColorListViewModelFactory

object InjectorUtils {
    private fun getColorRepository(context: Context): ColorRepository {
        return ColorRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).colorDao()
        )
    }

    fun provideColorListViewModelFactory(fragment: Fragment): ColorListViewModelFactory {
        val repository = getColorRepository(fragment.requireContext())
        return ColorListViewModelFactory(repository, fragment)
    }
}