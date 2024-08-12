package com.example.tinytrades

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tinytrades.adapters.OrderAdapter
import com.example.tinytrades.database.AppDatabase
import com.example.tinytrades.database.CartDao
import com.example.tinytrades.database.ItemDao
import com.example.tinytrades.database.Order
import com.example.tinytrades.database.OrderDao
import com.example.tinytrades.database.ProfileDao
import com.example.tinytrades.database.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class YourOrders : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var profileDao: ProfileDao
    private lateinit var itemDao: ItemDao
    private lateinit var cartDao: CartDao
    private lateinit var orderDao: OrderDao

    private lateinit var buyerImage: ImageView
    private lateinit var username: TextView

    private lateinit var orderRecyclerView: RecyclerView
    private lateinit var orderRecyclerViewAdapter: OrderAdapter
//    private lateinit var cancelbtn: Button

    private lateinit var backbtn: ImageButton
    private lateinit var home: ImageButton
    private lateinit var explore: ImageButton
    private lateinit var sell: ImageButton
    private lateinit var profile: ImageButton

    private lateinit var buyerUsername: TextView
    private lateinit var usernameExtra: String
    private lateinit var titleExtra: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_orders)

        usernameExtra = intent.getStringExtra("USERNAME") ?: ""

        database = AppDatabase.getDatabase(applicationContext)
        userDao = database.userDao()
        profileDao = database.profileDao()
        itemDao = database.itemDao()
        cartDao = database.cartDao()
        orderDao = database.orderDao()

        buyerUsername = findViewById(R.id.buyerUsername)
        buyerUsername.text = usernameExtra

        buyerImage = findViewById(R.id.buyerImage)
        username = findViewById(R.id.username)
//        cancelbtn = findViewById(R.id.cancelbtn)

        orderRecyclerView = findViewById(R.id.orderRecycleView)

        backbtn = findViewById(R.id.backbtn)
        home = findViewById(R.id.home)
        explore = findViewById(R.id.explore)
        sell = findViewById(R.id.sell)
        profile = findViewById(R.id.profile)

        loadProfile()

        orderRecyclerView.layoutManager = LinearLayoutManager(this)
        orderRecyclerViewAdapter = OrderAdapter(mutableListOf()) { order ->
            cancelOrder(order)
        }
        orderRecyclerView.adapter = orderRecyclerViewAdapter
        loadOrders()

        backbtn.setOnClickListener {
            onBackPressed()
        }
        home.setOnClickListener {
            val homeIntent = Intent(this, MainActivity::class.java).apply {
                putExtra("USERNAME", usernameExtra)
            }
            startActivity(homeIntent)
        }
        explore.setOnClickListener {
            val exploreIntent = Intent(this, MainActivity::class.java).apply {
                putExtra("navigate_to", "explore")
                putExtra("USERNAME", usernameExtra)
            }
            startActivity(exploreIntent)
        }
        sell.setOnClickListener {
            val sellIntent = Intent(this, SellActivity::class.java).apply {
                putExtra("USERNAME", usernameExtra)
            }
            startActivity(sellIntent)
        }
        profile.setOnClickListener {
            val profileIntent = Intent(this, ProfileActivity::class.java).apply {
                putExtra("USERNAME", usernameExtra)
            }
            startActivity(profileIntent)
        }
    }

    private fun cancelOrder(order: Order) {

    }

    private fun loadProfile() {
        lifecycleScope.launch {
            val profile = withContext(Dispatchers.IO) {
                profileDao.getProfileByUsername(usernameExtra)
            }
            if(profile == null) {
                showToast("profile not found")
            }
            else {
                username.text = profile.username
                buyerUsername.text = profile.username

                buyerImage.setImageResource(
                    if(profile.gender == "Male") R.drawable.men else R.drawable.women
                )
            }
        }
    }

    private fun loadOrders() {
        lifecycleScope.launch {
            val orders = withContext(Dispatchers.IO) {
                orderDao.getOrderByUsername(usernameExtra)
            }
            Log.d("Orders", "Number of orders fetched: ${orders.size}")
            if(orders.isEmpty()) {
                showToast("you have no orders")
            }
            else {
                orderRecyclerViewAdapter.updateOrders(orders)
            }
        }
    }



    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}