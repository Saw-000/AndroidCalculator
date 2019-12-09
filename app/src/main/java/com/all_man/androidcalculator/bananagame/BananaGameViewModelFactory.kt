package com.all_man.androidcalculator.bananagame

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.all_man.androidcalculator.database.AppleImageDatabaseDao

class BananaGameViewModelFactory(
    private val dataSource: AppleImageDatabaseDao,
    private val application: Application,
    private val TappedNum: Int): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BananaGameViewModel::class.java)) {
            return BananaGameViewModel(dataSource, application, TappedNum) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}