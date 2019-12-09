package com.all_man.androidcalculator

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.all_man.androidcalculator.database.AppleImageDatabase
import com.all_man.androidcalculator.database.AppleImageDatabaseDao
import com.all_man.androidcalculator.database.AppleImageInfo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import org.junit.Assert.assertEquals

@RunWith(AndroidJUnit4::class)
class AppleImageDatabaseTest {

    private lateinit var infoDao: AppleImageDatabaseDao
    private lateinit var db: AppleImageDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, AppleImageDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        infoDao = db.appleImageDatabaseDao
    }
    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }


    /** Test DAO methods **/
    /** Test毎にdatabaseはリセットされる！(確認済み) */

    @Test
    @Throws(Exception::class)
    fun checkBulkInsert() {
        infoDao.bulkInsert(
            (1..5).toList()
            .shuffled()
            .mapIndexed{ index, it ->
                AppleImageInfo(index, it, false)
            }
        )
        assertEquals(infoDao.countAllRows(), 5)
    }

}