package com.example.tinytrades.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ItemDao {
    @Insert
    fun insert(item: Item)

    @Query("select * from item where title = :title")
    fun getItemByTitle(title: String): Item?
}
