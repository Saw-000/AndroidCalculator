package com.all_man.androidcalculator.calculator

import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {

    //TODO: ボタンの文字を蓄える変数
    //private var lastCharactor : String

    private val _calcText = MutableLiveData<String>()
    val calcText : LiveData<String>
        get() = _calcText

    init{
        _calcText.value = "0"
    }

    fun addCharactorToCalculateText(view: View) {
        val textView = view as TextView
        _calcText.value = calcText.value + textView.text.toString()
    }


}