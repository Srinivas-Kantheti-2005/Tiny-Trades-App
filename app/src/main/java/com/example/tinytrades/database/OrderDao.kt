package com.example.tinytrades.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface OrderDao {

    @Insert
    suspend fun insertBuy(order: Order)

    @Update
    suspend fun updateBuy(order: Order)

    @Delete
    suspend fun deleteBuy(order: Order)
}