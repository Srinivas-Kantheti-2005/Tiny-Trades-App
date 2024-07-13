package com.example.tinytrades.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProfileDao {
    @Insert
    fun insert(profile: Profile)

    @Query("select * from Profile where emailid = :emailid")
    fun getProfileByEmailId(emailid: String): Profile?
}
