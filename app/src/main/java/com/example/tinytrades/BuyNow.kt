package com.example.tinytrades

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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

    private lateinit var profileImage: ImageView
    private lateinit var buyerUsername: TextView
    private lateinit var buyerName: TextView
    private lateinit var gender: TextView
    private lateinit var mobileNo: TextView
    private lateinit var emailId: TextView

    private lateinit var dNo: TextView
    private lateinit var street: TextView
    private lateinit var village: TextView
    private lateinit var pinCode: TextView
    private lateinit var mandal: TextView
    private lateinit var district: TextView

    private lateinit var cartImage: ImageView
    private lateinit var title: TextView
    private lateinit var size: TextView
    private lateinit var quantity: EditText
    private lateinit var price: TextView

    private lateinit var confirmBuy: Button
    private lateinit var update: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_now)

        database = AppDatabase.getDatabase(applicationContext)
        userDao = database.userDao()
        profileDao = database.profileDao()
        itemDao = database.itemDao()
        cartDao = database.cartDao()
        orderDao = database.orderDao()

        profileImage = findViewById(R.id.profileimage)
        buyerUsername = findViewById(R.id.username)
        buyerName = findViewById(R.id.buyerName)
        gender = findViewById(R.id.gender)
        mobileNo = findViewById(R.id.mobileno)
        emailId = findViewById(R.id.emailid)

        dNo = findViewById(R.id.dno)
        street = findViewById(R.id.street)
        village = findViewById(R.id.village)
        pinCode = findViewById(R.id.pincode)
        mandal = findViewById(R.id.mandal)
        district = findViewById(R.id.district)

        cartImage = findViewById(R.id.cart_image)
        title = findViewById(R.id.cart_title)
        size = findViewById(R.id.cart_size)
        quantity = findViewById(R.id.cart_quantity)
        price = findViewById(R.id.cart_price)

        confirmBuy = findViewById(R.id.confirm_buy)
        update = findViewById(R.id.update)
    }

    private fun loadProfile() {}

    private fun loadAddress() {}

    private fun loadItem() {}
}