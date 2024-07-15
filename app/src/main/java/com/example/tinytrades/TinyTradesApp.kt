package com.example.tinytrades

import android.app.Application
import androidx.room.Room
import com.example.tinytrades.database.AppDatabase

class TinyTradesApp : Application() {

    companion object {
        lateinit var database: AppDatabase
            private set
    }
    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "tinytrades-database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
