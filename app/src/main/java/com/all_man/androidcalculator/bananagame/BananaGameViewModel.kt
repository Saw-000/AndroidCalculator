package com.all_man.androidcalculator.bananagame

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class BananaGameViewModel(tappedNum: Int): ViewModel() {

    private var imgNumberList : MutableList<kotlin.collections.ArrayList<Any>>
    private var _imgNumberListLive = MutableLiveData<List<kotlin.collections.ArrayList<Any>>>()
    val imgNumberListLive : LiveData<List<kotlin.collections.ArrayList<Any>>>
        get() = _imgNumberListLive



    init {
        imgNumberList =
            (1..tappedNum).toList()
                .map { arrayListOf(it, false) }
                .shuffled()
                .toMutableList()
        _imgNumberListLive.value = imgNumberList
    }

    fun setImageNum(position: Int, imgNum: Int, displayWrongText: Boolean) {
        imgNumberList[position][0] = imgNum
        imgNumberList[position][1] = displayWrongText
        _imgNumberListLive.value = imgNumberList
    }
}