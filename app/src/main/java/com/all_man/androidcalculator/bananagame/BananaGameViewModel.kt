package com.all_man.androidcalculator.bananagame

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BananaGameViewModel(tappedNum: Int): ViewModel() {

    private var imgNumberList : MutableList<Int>
    private var _imgNumberListLive = MutableLiveData<List<Int>>()
    val imgNumberListLive : LiveData<List<Int>>
        get() = _imgNumberListLive



    init {
        imgNumberList = (1..tappedNum).toList().shuffled().toMutableList()
        _imgNumberListLive.value = imgNumberList
    }
}