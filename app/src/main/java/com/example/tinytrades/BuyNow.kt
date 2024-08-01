package com.example.tinytrades

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tinytrades.database.AppDatabase
import com.example.tinytrades.database.CartDao
import com.example.tinytrades.database.ItemDao
import com.example.tinytrades.database.OrderDao
import com.example.tinytrades.database.ProfileDao
import com.example.tinytrades.database.UserDao

class BuyNow : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var profileDao: ProfileDao
    private lateinit var itemDao: ItemDao
    private lateinit var cartDao: CartDao
    private lateinit var orderDao: OrderDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_now)

        database = AppDatabase.getDatabase(applicationContext)
        userDao = database.userDao()
        profileDao = database.profileDao()
        itemDao = database.itemDao()
        cartDao = database.cartDao()
        orderDao = database.orderDao()
    }
}