package com.all_man.androidcalculator.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AppleImageDatabaseDao {


    /** How to bulk_insert..?! */
    @Insert
    fun bulkInsert(infoList: List<AppleImageInfo>)

    @Update
    fun update(info: AppleImageInfo)

    @Query("SELECT * FROM apple_image_info_table ORDER BY dataId DESC")
    fun getAllinfo(): LiveData<List<AppleImageInfo>>

    @Query("DELETE FROM apple_image_info_table")
    fun clear()

    @Query("SELECT count(*) FROM apple_image_info_table")
    fun countAllRows(): Int
}