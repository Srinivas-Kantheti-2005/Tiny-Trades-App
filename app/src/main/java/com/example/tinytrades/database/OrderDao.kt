package com.example.tinytrades.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface OrderDao {
    @Insert
    suspend fun insert(order: Order)

    @Update
    suspend fun update(order: Order)

    @Delete
    suspend fun delete(order: Order)

    @Query("select * from `order` where buyerUsername = :username")
    suspend fun getOrderByUsername(username: String): List<Order>

    @Query("select * from `order` where title = :itemTitle")
    suspend fun getOrderByTitle( itemTitle: String): List<Order>

    @Query("select * from `order` where buyerUsername = :username and title = :itemTitle")
    abstract fun getOrderByUsernameAndTitle(username: String, itemTitle: String): List<Order>
}
