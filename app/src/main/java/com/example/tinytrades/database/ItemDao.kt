package com.example.tinytrades.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ItemDao {
    @Insert
    fun insert(item: Item)

    @Query("select * from item where title = :title")
    fun getItemByTitle(title: String): Item?

    @Update
    fun update(item: Item)

    @Delete
    fun delete(item: Item)
}
