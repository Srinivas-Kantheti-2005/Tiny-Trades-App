package com.example.tinytrades.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class, Profile::class, Item::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun profileDao(): ProfileDao
    abstract fun itemDao(): ItemDao
}
