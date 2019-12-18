package com.all_man.androidcalculator.bananagame2

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlin.random.Random

class BananaGame2ViewModel :  ViewModel() {
    // Coroutine Scope
    var viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // Navigation trigger to GameOver/Clear
    private val _navGameOver = MutableLiveData<Boolean>()
    val navGameOver : LiveData<Boolean>
        get() =  _navGameOver
    private val _navClear = MutableLiveData<Boolean>()
    val navClear : LiveData<Boolean>
        get() =  _navClear

    // image touchability
    private val _clickableFlag = MutableLiveData<Boolean>()
    val clickableFlag : LiveData<Boolean>
        get() = _clickableFlag
    // ImageUrl LiveData
    private val _imageUrl = MutableLiveData<String>()
    val imageUrl : LiveData<String>
        get() =  _imageUrl

    // ImageUrlList
    val imageUriList = listOf<String>(
        //peach
        "https://www.photolibrary.jp/mhd7/img207/450-20110529140446138470.jpg",
        //pear
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTG6TLgBDFBkt1Bzmb7F7_QBm0QqFEQypoj-34GZP30aXz4xpP0&s",
        //kiwi
        "https://image.jimcdn.com/app/cms/image/transf/none/path/s3b67276d0eccefb9/image/i7d8ede2bec1a2611/version/1557813553/image.jpg"
    )


    // Timer
    companion object {
        private const val DONE = 0L
        private const val ONE_SECOND = 1000L
        private const val COUNTDOWN_TIME = 180000L
    }
    // Countdown time
    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime
    val currentTimeString =Transformations.map(currentTime) {
        DateUtils.formatElapsedTime(it)
    }
    private val timer: CountDownTimer


    init {
        _navGameOver.value = false
        _clickableFlag.value = true
        // set default image to Peach
        _imageUrl.value = imageUriList[0]

        // Countdown timer
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = millisUntilFinished/ONE_SECOND
            }
            override fun onFinish() {
                _currentTime.value = DONE
                onNavGameOver()
            }
        }
        timer.start()
    }


    // navigation to GameOver Fragment
    fun onNavGameOver() {
        uiScope.launch {
            delay(1500)
            _navGameOver.value = true
        }
    }
    fun finishNavGameOver() {
        _navGameOver.value = false
    }

    fun onNavClear() {
        uiScope.launch {
            delay(1500)
            _navClear.value = true
        }
    }
    fun finishNavClear() {
        _navClear.value = false
    }

    fun clickabilityTofalse() {
        _clickableFlag.value = false
    }
    // set grid Image
    fun setImageUrl() {
        _imageUrl.value =
            when(Random.nextInt(10)) {
            0 -> imageUriList[1]
            1 -> imageUriList[2]
            else -> imageUriList[0]
        }
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

}