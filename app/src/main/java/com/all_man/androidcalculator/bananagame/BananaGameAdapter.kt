package com.all_man.androidcalculator.bananagame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.all_man.androidcalculator.R
import com.all_man.androidcalculator.databinding.ListItemBananaGameBinding


class BananaGameAdapter : RecyclerView.Adapter<BananaGameAdapter.ViewHolder>() {

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
        val item = Pair(position, data[position])

        holder.bind(item)
    }


    class ViewHolder private constructor(val binding: ListItemBananaGameBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: Pair<Int, Int>
        ) {
            binding.tappedAppleImage.setImageResource(R.drawable.one_apple)
            binding.myListItem.setOnClickListener {

                binding.tappedAppleImage.setImageResource(
                    when (item.second) {
                        1 -> {
                            binding.informWrongText.visibility = View.INVISIBLE
                            R.drawable.one_banana
                        }
                        2 -> {
                            binding.informWrongText.visibility = View.VISIBLE
                            R.drawable.banana_peel
                        }
                        else -> {
                            binding.informWrongText.visibility = View.INVISIBLE
                            R.drawable.apple_core
                        }
                    }
                )
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
