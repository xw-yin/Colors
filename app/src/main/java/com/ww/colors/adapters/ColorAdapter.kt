package com.ww.colors.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ww.colors.data.Color
import com.ww.colors.databinding.ItemColorBinding
import com.ww.colors.view.ColorListFragment
import com.ww.colors.view.ColorListFragmentDirections

class ColorAdapter : ListAdapter<Color, RecyclerView.ViewHolder>(ColorDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ColorViewHolder(ItemColorBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       val color=getItem(position)
        (holder as ColorViewHolder).bind(color)
    }


}

class ColorViewHolder(
        private val binding: ItemColorBinding
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.setClicklistener {
            binding.color?.let { color ->
                navigateToColorDetail(color,it)
            }
        }
    }

    private fun navigateToColorDetail(
            color: Color,
            view: View
    ) {
        val direction=ColorListFragmentDirections.actionColorListFragmentToColorDetailFragment(color.colorId)
        view.findNavController().navigate(direction)
    }
    fun bind(item:Color){
        binding.apply {
            color=item
            executePendingBindings()
        }
    }
}

private class ColorDiffCallback : DiffUtil.ItemCallback<Color>() {
    override fun areItemsTheSame(oldItem: Color, newItem: Color): Boolean {
        return oldItem.colorId == newItem.colorId
    }

    override fun areContentsTheSame(oldItem: Color, newItem: Color): Boolean {
        return oldItem == newItem
    }

}