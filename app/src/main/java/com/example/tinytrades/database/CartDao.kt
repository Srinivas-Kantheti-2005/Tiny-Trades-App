package com.example.tinytrades.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CartDao {
    @Insert
    suspend fun insert(cart: Cart)

    @Update
    suspend fun update(cart: Cart)

    @Delete
    suspend fun delete(cart: Cart)

    @Query("SELECT * FROM cart WHERE buyerUsername = :buyerUsername")
    suspend fun getCartItemsByBuyer(buyerUsername: String): List<Cart>

    @Query("select * from cart where title = :itemTitle")
    suspend fun getCartItemByTitle(itemTitle: String): Cart?

    @Query("select * from cart where id = :itemId")
    suspend fun getCartItemsById(itemId: Int): Cart?
}