package com.all_man.androidcalculator.bananagame

import android.text.method.NumberKeyListener
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.all_man.androidcalculator.R
import com.all_man.androidcalculator.databinding.ListItemBananaGameBinding


class BananaGameAdapter(val clickListener: RecyclerViewItemListener) : RecyclerView.Adapter<BananaGameAdapter.ViewHolder>() {

    var data = listOf<Int>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    override fun getItemCount() = data.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listOf(position, data[position])

        holder.bind(item, clickListener)
    }


    class ViewHolder private constructor(val binding: ListItemBananaGameBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: List<Int>,
            clickListener: RecyclerViewItemListener
        ) {
            clickListener.numList = item
            binding.clickListener = clickListener
            binding.tappedAppleImage.setImageResource(
                when (item[1]) {
                    -1 -> R.drawable.one_banana
                    -2 -> R.drawable.banana_peel
                    0 -> R.drawable.apple_core
                    else -> R.drawable.one_apple
                }
            )
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


class RecyclerViewItemListener(var clickListener: (imgNum: List<Int>) -> Unit) {
    var numList = listOf(0,0)
    fun onClick(imgNumber: List<Int>) = clickListener(imgNumber)
}