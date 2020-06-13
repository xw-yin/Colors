package com.ww.colors.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ww.colors.databinding.FragmentColorDetailBinding
import com.ww.colors.utilities.InjectorUtils
import com.ww.colors.viewmodels.ColorDetailViewModel

class ColorDetailFragment : Fragment() {
    private val args: ColorDetailFragmentArgs by navArgs()
    private val colorDetailViewModel: ColorDetailViewModel by viewModels {
        InjectorUtils.provideColorDetailViewModelFactory(this,args.colorId)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentColorDetailBinding.inflate(inflater, container, false).apply {
            viewModel=colorDetailViewModel
            lifecycleOwner=viewLifecycleOwner
        }
        return binding.getRoot()
    }
}