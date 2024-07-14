package com.example.tinytrades.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProfileDao {
    @Insert
    fun insert(profile: Profile)

    @Query("select * from Profile where emailid = :emailid")
    fun getProfileByEmailId(emailid: String): Profile?

    @Query("select * from Profile where username = :username LIMIT 1")
    suspend fun getProfileByUsername(username: String): Profile

    @Delete
    fun delete(profile: Profile)

    @Query("delete from profile where emailid = :emailid")
    fun deleteByEmailId(emailid: String)

    @Update
    fun update(profile: Profile)
}
