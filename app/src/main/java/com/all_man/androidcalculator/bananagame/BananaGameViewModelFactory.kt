package com.all_man.androidcalculator.bananagame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BananaGameViewModelFactory(private val TappedNum: Int): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BananaGameViewModel::class.java)) {
            return BananaGameViewModel(TappedNum) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}