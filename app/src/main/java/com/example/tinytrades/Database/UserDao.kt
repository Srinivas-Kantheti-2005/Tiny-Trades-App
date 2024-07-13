package com.example.tinytrades.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("select * from User where Username = :username")
    suspend fun getUserByUsername(username: String): User?
}
