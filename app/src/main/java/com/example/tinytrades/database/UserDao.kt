package com.example.tinytrades.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("select * from User where Username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    @Delete
    suspend fun delete(user: User)

    @Update
    suspend fun update(user: User)
}
