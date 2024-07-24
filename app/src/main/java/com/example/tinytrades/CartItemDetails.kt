package com.example.tinytrades

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tinytrades.database.AppDatabase
import com.example.tinytrades.database.CartDao
import com.example.tinytrades.database.ItemDao
import com.example.tinytrades.database.ProfileDao
import com.example.tinytrades.database.UserDao

class CartItemDetails : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var profileDao: ProfileDao
    private lateinit var itemDao: ItemDao
    private lateinit var cartDao: CartDao

    private lateinit var backbtn: ImageButton

    private lateinit var image: ImageView
    private lateinit var title: TextView
    private lateinit var decrement: ImageButton
    private lateinit var quantity: TextView
    private lateinit var increment: ImageButton
    private lateinit var price: TextView
    private lateinit var size: TextView

    private lateinit var buynow: Button
    private lateinit var delete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_item_details)

        database = AppDatabase.getDatabase(applicationContext)
        userDao = database.userDao()
        profileDao = database.profileDao()
        itemDao = database.itemDao()
        cartDao = database.cartDao()

        backbtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}