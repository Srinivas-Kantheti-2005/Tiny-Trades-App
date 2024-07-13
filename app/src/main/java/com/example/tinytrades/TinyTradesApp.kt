package com.example.tinytrades

import android.app.Application
import androidx.room.Room
import com.example.tinytrades.database.AppDatabase

class TinyTradesApp : Application() {
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "tinytrades-database"
        ).build()
    }
}
