package com.all_man.androidcalculator.bananagame

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.all_man.androidcalculator.database.AppleImageDatabaseDao
import com.all_man.androidcalculator.database.AppleImageInfo
import kotlinx.coroutines.*

class BananaGameViewModel(dataSource: AppleImageDatabaseDao,
                          application: Application,
                          tappedNum: Int): AndroidViewModel(application) {

    val database = dataSource

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val allImageInfo = database.getAllinfo()

    init {
        initializeDatabase(tappedNum)
    }

    // database初期化->電卓から受け取った値分のimageinfoを作成
    private fun initializeDatabase(tappedNum: Int) {
        uiScope.launch {
            clear()
            insertAllInfo(tappedNum)
        }
    }
    private suspend fun insertAllInfo(tappedNum: Int) {
        withContext(Dispatchers.IO) {
            database.bulkInsert(
                (1..tappedNum)
                .toList()
                .shuffled()
                .mapIndexed{ index, it ->
                    AppleImageInfo(index, it, false)
                }
            )
        }
    }

    // recyclerViewのitemのClickLisnerから呼ばれる関数
    // タッチされた画像の
    fun onSetAppleInfo(position: Int, imgNum: Int, displayWrongText: Boolean) {
        uiScope.launch {
            setAppleInfo(position, imgNum, displayWrongText)
        }
    }
    private suspend fun setAppleInfo(position: Int, imgNum: Int, displayWrongText: Boolean) {
        withContext(Dispatchers.IO) {
            database.update(AppleImageInfo(position, imgNum, displayWrongText))
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    /** onClearedってどのthreadで実行？
     *  viewModel破棄でdatabase.clear()は可能？
     *  ＝＞(database.clear()をI/O threadで行いたいなぁ)*/
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}