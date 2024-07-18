package com.example.tinytrades

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tinytrades.database.AppDatabase
import com.example.tinytrades.database.ItemDao
import com.example.tinytrades.database.Profile
import com.example.tinytrades.database.ProfileDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SellerProfileActivity : AppCompatActivity() {

    private lateinit var profileDao: ProfileDao
    private lateinit var itemDao: ItemDao
    private lateinit var database: AppDatabase

    private lateinit var backbtn: ImageButton
    private lateinit var sellerImage: ImageView
    private lateinit var sellerName: TextView
    private lateinit var sellerRecyclerView: RecyclerView

    private lateinit var sellerRecyclerViewAdapter: SellerRecyclerViewAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_profile)

        // Initialize the database and DAOs
        database = AppDatabase.getDatabase(applicationContext)
        profileDao = database.profileDao()
        itemDao = database.itemDao()

        // Initialize UI components
        backbtn = findViewById(R.id.backbtn)
        sellerImage = findViewById(R.id.sellerImage)
        sellerName = findViewById(R.id.sellername)
        sellerRecyclerView = findViewById(R.id.sellerRecyclerView)

        // Retrieve the username from the intent
        val username = intent.getStringExtra("USERNAME") ?: ""

        // Set up the RecyclerView
        sellerRecyclerViewAdapter = SellerRecyclerViewAdapter(mutableListOf())
        sellerRecyclerView.adapter = sellerRecyclerViewAdapter
        sellerRecyclerView.layoutManager = GridLayoutManager(this, 3)

        // Load the seller profile and items
        loadSellerProfile(username)
        loadSellerItems(username)

        // Set the back button click listener
        backbtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loadSellerProfile(username: String) {
        lifecycleScope.launch {
            try {
                val profile = withContext(Dispatchers.IO) {
                    profileDao.getProfileByUsername(username)
                }
                if (profile != null) {
                    populateSellerProfile(profile)
                } else {
                    showToast("Profile not found")
                }
            } catch (e: Exception) {
                showToast("Error loading profile: ${e.message}")
            }
        }
    }

    private fun populateSellerProfile(profile: Profile) {
        sellerImage.setImageResource(profile.image)
        sellerName.text = "${profile.firstname} ${profile.lastname}"
    }

    private fun loadSellerItems(username: String) {
        lifecycleScope.launch {
            try {
                val sellerItems = withContext(Dispatchers.IO) {
                    itemDao.getItemsBySeller(username)
                }
                sellerRecyclerViewAdapter.updateItems(sellerItems)
            } catch (e: Exception) {
                showToast("Error loading items: ${e.message}")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
