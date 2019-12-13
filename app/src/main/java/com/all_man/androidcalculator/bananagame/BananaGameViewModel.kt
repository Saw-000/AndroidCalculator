package com.all_man.androidcalculator.bananagame

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.all_man.androidcalculator.database.AppleImageDatabaseDao
import com.all_man.androidcalculator.database.AppleImageInfo
import kotlinx.coroutines.*

class BananaGameViewModel(dataSource: AppleImageDatabaseDao,
                          application: Application,
                          tappedNum: Int): AndroidViewModel(application) {

    private val imageNum = tappedNum
    val database = dataSource

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val allImageInfo = database.getAllinfo()

    // Controlling the navigation to ClearFragment
    private val _navigateToClearFragment = MutableLiveData<Int>()
    val navigateToClearFragment: LiveData<Int>
        get() = _navigateToClearFragment
    // Controlling the navigation to GameOverFragment
    private val _navigateToGameOverFragment = MutableLiveData<Int>()
    val navigateToGameOverFragment: LiveData<Int>
        get() = _navigateToGameOverFragment

    // flag for clickability of recyclerView_items
    private val _flag = MutableLiveData<Boolean>()
    val flag: LiveData<Boolean>
        get() = _flag

    init {
        initializeDatabase(tappedNum)
        _navigateToClearFragment.value = null
        _navigateToGameOverFragment.value = null
<<<<<<< HEAD
=======
        _flag.value = true

        // timer
        timer = object: CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onTick(p0: Long) {
                _currentTime.value = p0 / ONE_SECOND
            }
            override fun onFinish() {
                _currentTime.value = DONE
                onNavigateToGameOverFragment()
            }
        }
        timer.start()
>>>>>>> 49cf457... Add nonclickability during one_item clicked.
    }

    // database初期化->電卓から受け取った値分のimageinfoを作成
    private fun initializeDatabase(Num: Int) {
        uiScope.launch {
            clear()
            insertAllInfo(Num)
        }
    }
//        fun onClear() {
//            uiScope.launch { clear() }
//        }
    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }
    private suspend fun insertAllInfo(Num: Int) {
        withContext(Dispatchers.IO) {
            database.bulkInsert(
                (1..Num)
                .toList()
                .shuffled()
                .mapIndexed{ index, it ->
                    AppleImageInfo(index, it, false)
                }
            )
        }
    }


    // recyclerViewのitemのClickListenerから呼ばれる関数
    // タッチされた画像のdatabase内リソースidを変更する処理をスタート
    fun onSetAppleInfo(position: Int, imgNum: Int, displayWrongText: Boolean){
        uiScope.launch {
            setAppleInfo(position, imgNum, displayWrongText)
        }
    }
    // タッチされた画像のdatabase内リソースidを変更する処理
    private suspend fun setAppleInfo(position: Int, imgNum: Int, displayWrongText: Boolean) {
        withContext(Dispatchers.IO) {
            database.update(AppleImageInfo(position, imgNum, displayWrongText))
        }
    }




    // When right-image is clicked, change _navigateToClearFragment.value to true.
    fun onNavigateToClearFragment() {
        uiScope.launch {
            delay(1500)
            _navigateToClearFragment.value = imageNum
        }
    }
<<<<<<< HEAD
    // When the Navigation is finished, change _navigateToClearFragment.value to false.
    fun onFinishNavigateToClearFragment() { _navigateToClearFragment.value = null }
=======
    // When the Navigation is finished, change _navigateToClearFragment.value to false
    // for the navigation.
    fun onFinishNavigateToClearFragment() {
        _navigateToClearFragment.value = null
        flagTrue()
    }
>>>>>>> 49cf457... Add nonclickability during one_item clicked.

    // When wrong-image is clicked or time is up, change _navigateToGameOverFragment.value to true.
    fun onNavigateToGameOverFragment() {
        uiScope.launch {
            delay(1500)
            _navigateToGameOverFragment.value = imageNum
        }
    }
    // When the Navigation is finished, change _navigateToGameOverFragment.value to false.
    fun onFinishNavigateToGameOverFragment() { _navigateToGameOverFragment.value = null }

    fun flagTrue() { _flag.value = true }
    fun flagFalse() { _flag.value = false }

    /** onClearedってどのthreadで実行？
     *  viewModel破棄でdatabase.clear()は可能？
     *  ＝＞(database.clear()をI/O threadで行いたいなぁ)*/
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}