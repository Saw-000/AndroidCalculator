package com.all_man.androidcalculator.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "apple_image_info_table")
data class AppleImageInfo(
    @PrimaryKey(autoGenerate = false)
    var dataId: Int,
    /** varじゃないとダメかな？ */

    @ColumnInfo(name = "image_number")
    var imageNumber: Int,

    @ColumnInfo(name = "display_wrong_text")
    var displayWrongText: Boolean
)