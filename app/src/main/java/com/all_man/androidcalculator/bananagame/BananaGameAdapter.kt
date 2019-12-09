package com.all_man.androidcalculator.bananagame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.all_man.androidcalculator.R
import com.all_man.androidcalculator.database.AppleImageInfo
import com.all_man.androidcalculator.databinding.ListItemBananaGameBinding


class BananaGameAdapter(val clickListener: RecyclerViewItemListener) :
    ListAdapter<AppleImageInfo, BananaGameAdapter.ViewHolder>(BananaGameDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }


    class ViewHolder private constructor(val binding: ListItemBananaGameBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AppleImageInfo, clickListener: RecyclerViewItemListener) {
            binding.appleImageInfo = item
            binding.clickListener = clickListener
            binding.tappedAppleImage.setImageResource(
                when (item.imageNumber) {
                    0 -> R.drawable.apple_core
                    -1 -> R.drawable.one_banana
                    -2 -> R.drawable.banana_peel
                    else -> R.drawable.one_apple
                }
            )
            binding.informWrongText.visibility =
                if (item.displayWrongText) View.VISIBLE else View.INVISIBLE
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemBananaGameBinding.inflate(layoutInflater, parent,false)
                return ViewHolder(binding)
            }
        }
    }
}

class BananaGameDiffCallback : DiffUtil.ItemCallback<AppleImageInfo>() {
    override fun areItemsTheSame(oldItem: AppleImageInfo, newItem: AppleImageInfo): Boolean {
        return oldItem.dataId == newItem.dataId
    }
    override fun areContentsTheSame(oldItem: AppleImageInfo, newItem: AppleImageInfo): Boolean {
        return oldItem == newItem
    }

}

class RecyclerViewItemListener(var clickListener: (info: AppleImageInfo) -> Unit) {
    fun onClick(info: AppleImageInfo) = clickListener(info)
}
