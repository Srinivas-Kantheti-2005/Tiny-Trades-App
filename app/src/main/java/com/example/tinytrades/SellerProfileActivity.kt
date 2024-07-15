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
import androidx.room.Room
import com.example.tinytrades.database.AppDatabase
import com.example.tinytrades.database.ItemDao
import com.example.tinytrades.database.Profile
import com.example.tinytrades.database.ProfileDao
import com.example.tinytrades.database.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SellerProfileActivity : AppCompatActivity() {

    private lateinit var userDao: UserDao
    private lateinit var profileDao: ProfileDao
    private lateinit var itemDao: ItemDao
    private lateinit var database: AppDatabase

    private lateinit var backbtn: ImageButton
    private lateinit var sellerimage: ImageView
    private lateinit var sellername: TextView
    private lateinit var sellerrecyclerview: RecyclerView

    private lateinit var sellerRecyclerViewAdapter: SellerRecyclerViewAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_profile)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "tinytrades-database"
        )
            .fallbackToDestructiveMigration()
            .build()

        userDao = database.userDao()
        profileDao = database.profileDao()
        itemDao = database.itemDao()

        backbtn = findViewById(R.id.backbtn)
        sellerimage = findViewById(R.id.sellerImage)
        sellername = findViewById(R.id.sellername)
        sellerrecyclerview = findViewById(R.id.sellerRecyclerView)

        backbtn.setOnClickListener {
            onBackPressed()
        }

        sellerRecyclerViewAdapter = SellerRecyclerViewAdapter(mutableListOf())
        sellerrecyclerview.adapter = sellerRecyclerViewAdapter
        sellerrecyclerview.layoutManager = GridLayoutManager(this, 3)

        // Retrieve username passed from LoginPage
        val username = intent.getStringExtra("USERNAME") ?: ""
        loadSellerProfile(username)
        loadSellerItems(username)
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
        // Set seller image based on profile image field
        sellerimage.setImageResource(profile.image)
        // Set seller name
        sellername.text = "${profile.firstname} ${profile.lastname}"
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
