package com.ww.colors.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.ww.colors.adapters.ColorAdapter
import com.ww.colors.databinding.FragmentColorListBinding
import com.ww.colors.utilities.InjectorUtils
import com.ww.colors.viewmodels.ColorListViewModel

class ColorListFragment : Fragment() {
    private val colorListViewModel: ColorListViewModel by viewModels {
        InjectorUtils.provideColorListViewModelFactory(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentColorListBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val colorAdapter = ColorAdapter()
        binding.colorList.adapter = colorAdapter
        subscribeUI(colorAdapter)

        return binding.root
    }

    private fun subscribeUI(adapter: ColorAdapter) {
        colorListViewModel.colors.observe(viewLifecycleOwner, Observer{ colors ->
            adapter.submitList(colors)
        })
    }
}