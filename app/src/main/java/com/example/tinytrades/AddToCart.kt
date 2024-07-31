package com.example.tinytrades

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tinytrades.database.AppDatabase
import com.example.tinytrades.database.Cart
import com.example.tinytrades.database.CartDao
import com.example.tinytrades.database.ItemDao
import com.example.tinytrades.database.Profile
import com.example.tinytrades.database.ProfileDao
import com.example.tinytrades.database.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddToCart : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var profileDao: ProfileDao
    private lateinit var itemDao: ItemDao
    private lateinit var cartDao: CartDao

    private lateinit var backbtn: ImageButton
    private lateinit var buyerImage: ImageView
    private lateinit var buyerName: TextView
    private lateinit var addToCartRecyclerView: RecyclerView

    private lateinit var cartAdapter: CartAdapter

    @SuppressLint("SetTextI18n", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_cart)

        database = AppDatabase.getDatabase(applicationContext)
        userDao = database.userDao()
        profileDao = database.profileDao()
        itemDao = database.itemDao()
        cartDao = database.cartDao()

        backbtn = findViewById(R.id.backbtn)
        buyerImage = findViewById(R.id.buyerImage)
        buyerName = findViewById(R.id.buyerName)
        addToCartRecyclerView = findViewById(R.id.add_to_cart_recycler_view)

        cartAdapter = CartAdapter(mutableListOf(), { cartItem, updatedQuantity ->
            updateCartItem(cartItem, updatedQuantity)
        }, { cartItem ->
            navigateToCartItemDetails(cartItem.title)
        }, { cartItem ->
            deleteCartItem(cartItem)
        })

        addToCartRecyclerView.layoutManager = LinearLayoutManager(this)
        addToCartRecyclerView.adapter = cartAdapter

        backbtn.setOnClickListener {
            onBackPressed()
        }

        val username = intent.getStringExtra("USERNAME") ?: ""
        if (username.isNotEmpty()) {
            loadProfile(username)
            loadCartItems(username)
        }
    }

    private fun navigateToCartItemDetails(itemTitle: String) {
        val intent = Intent(this, CartItemDetails::class.java).apply {
            putExtra("ITEM_TITLE", itemTitle)
        }
        startActivity(intent)
    }

    private fun loadProfile(username: String) {
        lifecycleScope.launch {
            try {
                val profile = withContext(Dispatchers.IO) {
                    profileDao.getProfileByUsername(username)
                }
                if (profile != null) {
                    populatedBuyerProfile(profile)
                } else {
                    showToast("Profile not found")
                }
            } catch (e: Exception) {
                showToast("Error loading profile: ${e.message}")
            }
        }
    }

    private fun populatedBuyerProfile(profile: Profile) {
        buyerImage.setImageResource(profile.image)
        buyerName.text = "${profile.firstname} ${profile.lastname}"
    }

    private fun loadCartItems(username: String) {
        lifecycleScope.launch {
            try {
                val items = withContext(Dispatchers.IO) {
                    cartDao.getCartItemsByBuyer(username)
                }
                cartAdapter.updateCart(items)
            } catch (e: Exception) {
                showToast("Error loading cart items: ${e.message}")
            }
        }
    }

    private fun updateCartItem(cartItem: Cart, newQuantity: Int) {
        lifecycleScope.launch {
            try {
                if (newQuantity > 0) {
                    withContext(Dispatchers.IO) {
                        val unitPrice = if (cartItem.quantity > 0) {
                            cartItem.price / cartItem.quantity
                        } else {
                            cartItem.price
                        }
                        val newPrice = unitPrice * newQuantity
                        val updatedCartItem = cartItem.copy(quantity = newQuantity, price = newPrice)
                        cartDao.update(updatedCartItem)
                    }
                    loadCartItems(intent.getStringExtra("USERNAME") ?: "")
                    showToast("Cart updated successfully")
                } else {
                    showToast("Quantity must be greater than zero")
                }
            } catch (e: Exception) {
                showToast("Error updating cart: ${e.message}")
            }
        }
    }



    private fun deleteCartItem(cartItem: Cart) {
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    cartDao.delete(cartItem)
                }
                loadCartItems(intent.getStringExtra("USERNAME") ?: "")
                showToast("Item removed from cart")
            } catch (e: Exception) {
                showToast("Error removing item from cart: ${e.message}")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        val username = intent.getStringExtra("USERNAME") ?: ""
        if (username.isNotEmpty()) {
            loadCartItems(username)
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
