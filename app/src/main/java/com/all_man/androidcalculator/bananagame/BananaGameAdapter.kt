package com.all_man.androidcalculator.bananagame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.all_man.androidcalculator.R
import com.all_man.androidcalculator.databinding.ListItemBananaGameBinding


class BananaGameAdapter(val clickListener: RecyclerViewItemListener) : RecyclerView.Adapter<BananaGameAdapter.ViewHolder>() {

    var data = listOf<kotlin.collections.ArrayList<Any>>()
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
        val item = Pair(position, data[position])

        holder.bind(item, clickListener)
    }


    class ViewHolder private constructor(val binding: ListItemBananaGameBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: Pair<Int, ArrayList<Any>>,
            clickListener: RecyclerViewItemListener
        ) {
            binding.clickListener = clickListener
            binding.holdNumPair = HoldNumPair(item)
            binding.tappedAppleImage.setImageResource(
                when (item.second[0]) {
                    0 -> R.drawable.apple_core
                    -1 -> R.drawable.one_banana
                    -2 -> R.drawable.banana_peel
                    else -> R.drawable.one_apple
                }
            )
            binding.informWrongText.visibility = when(item.second[1]) {
                true -> View.VISIBLE
                else -> View.INVISIBLE
            }
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

class RecyclerViewItemListener(var clickListener: (imgNumPair: Pair<Int, ArrayList<Any>>) -> Unit) {
    fun onClick(holdNumPair: HoldNumPair) = clickListener(holdNumPair.numPair)
}

data class HoldNumPair(
    val numPair : Pair<Int, ArrayList<Any>> = Pair(0, arrayListOf(0,false))
)