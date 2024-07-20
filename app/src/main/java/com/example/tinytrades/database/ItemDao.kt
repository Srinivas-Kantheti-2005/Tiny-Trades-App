package com.example.tinytrades.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ItemDao {
    @Insert
    suspend fun insert(item: Item)

    @Query("SELECT * FROM item")
    fun getAllItemsLive(): LiveData<List<Item>>

    @Query("select * from item")
    suspend fun getAllItems(): List<Item>

    @Query("SELECT * FROM item WHERE title LIKE :query")
    fun searchItems(query: String): List<Item>

    @Query("SELECT * FROM item WHERE username = :username")
    suspend fun getItemsBySeller(username: String): List<Item>

    @Query("select * from item where username = :username")
    suspend fun getItemByUsername(username: String): List<Item>

    @Query("SELECT * FROM item WHERE title = :title LIMIT 1")
    suspend fun getItemByTitle(title: String): Item?

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)
}
