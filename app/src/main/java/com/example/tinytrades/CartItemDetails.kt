package com.example.tinytrades

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tinytrades.database.AppDatabase
import com.example.tinytrades.database.Cart
import com.example.tinytrades.database.CartDao
import com.example.tinytrades.database.ItemDao
import com.example.tinytrades.database.ProfileDao
import com.example.tinytrades.database.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartItemDetails : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var profileDao: ProfileDao
    private lateinit var itemDao: ItemDao
    private lateinit var cartDao: CartDao

    private lateinit var backbtn: ImageButton

    private lateinit var image: ImageView
    private lateinit var title: TextView
    private lateinit var quantity: TextView
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

        backbtn = findViewById(R.id.backbtn)
        image = findViewById(R.id.cartDetailImage)
        title = findViewById(R.id.cartDetailTitle)
        quantity = findViewById(R.id.cartDetailquantity)
        price = findViewById(R.id.cartDetailPrice)
        size = findViewById(R.id.cartDetailSize)
        buynow = findViewById(R.id.buynow)
        delete = findViewById(R.id.delete)

        backbtn.setOnClickListener {
            onBackPressed()
        }

        val itemTitle = intent.getStringExtra("ITEM_TITLE") ?: ""
        if (itemTitle.isNotEmpty()) {
            loadItemDetails(itemTitle)
        }
    }

    private fun loadItemDetails(itemTitle: String) {
        lifecycleScope.launch {
            try {
                val item = withContext(Dispatchers.IO) {
                    cartDao.getCartItemByTitle(itemTitle)
                }
                if (item != null) {
                    displayItemDetails(item)
                } else {
                    showToast("Item not found")
                }
            } catch (e: Exception) {
                showToast("Error loading item details: ${e.message}")
            }
        }
    }

    private fun displayItemDetails(item: Cart) {
        item.image?.let {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            image.setImageBitmap(bitmap)
        } ?: image.setImageResource(android.R.color.transparent)
        title.text = item.title
        quantity.text = item.quantity.toString()
        price.text = item.price.toString()
        size.text = item.size
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
