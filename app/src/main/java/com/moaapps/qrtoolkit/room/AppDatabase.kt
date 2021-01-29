package com.moaapps.qrtoolkit.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.moaapps.qrtoolkit.modules.QRCode

@Database(entities = [QRCode::class], exportSchema = false, version = 1)
abstract class AppDatabase:RoomDatabase() {

    abstract fun historyDao(): HistoryDao

    companion object{
        fun getInstance(context: Context):AppDatabase{
            return Room.databaseBuilder(context, AppDatabase::class.java, "main")
                .build()
        }
    }
}