package com.all_man.androidcalculator.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AppleImageInfo::class], version = 1, exportSchema = false)
abstract class AppleImageDatabase : RoomDatabase() {

    abstract val appleImageDatabaseDao : AppleImageDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: AppleImageDatabase? = null

        fun getInstance(context: Context) : AppleImageDatabase {

            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppleImageDatabase::class.java,
                        "apple_image_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}