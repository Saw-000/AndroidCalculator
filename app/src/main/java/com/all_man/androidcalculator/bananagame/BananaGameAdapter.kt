package com.all_man.androidcalculator.bananagame

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.all_man.androidcalculator.R
import com.all_man.androidcalculator.database.AppleImageInfo
import com.all_man.androidcalculator.databinding.ListItemBananaGameBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class BananaGameAdapter(val clickListener: RecyclerViewItemListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(BananaGameDiffCallback()) {

    val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<AppleImageInfo>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.AppleImageInfoItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }

    }
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.AppleImageInfoItem -> ITEM_VIEW_TYPE_ITEM
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown ViewType: ${viewType}")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val appleImageInfoItem = getItem(position) as DataItem.AppleImageInfoItem
                holder.bind(appleImageInfoItem.appleImageInfo, clickListener)
            }
        }
    }


    class ViewHolder private constructor(val binding: ListItemBananaGameBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AppleImageInfo, clickListener: RecyclerViewItemListener) {
            binding.appleImageInfo = item
            binding.clickListener = clickListener
            binding.tappedAppleImage.setImageResource(
                when (item.imageNumber) {
                    0 -> R.drawable.fruits_apple_core
                    -1 -> R.drawable.fruits_one_banana
                    -2 -> R.drawable.fruits_banana_peel
                    else -> R.drawable.fruits_one_apple
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

    class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return TextViewHolder(view)
            }
        }
    }
}

class BananaGameDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

class RecyclerViewItemListener(var clickListener: (info: AppleImageInfo) -> Unit) {
    fun onClick(info: AppleImageInfo) = clickListener(info)
}

// for Header
sealed class DataItem {
    abstract val id: Int
    data class AppleImageInfoItem(val appleImageInfo: AppleImageInfo): DataItem() {
        override val id = appleImageInfo.dataId
    }

    object Header: DataItem(){
        override val id = Int.MIN_VALUE
    }
}