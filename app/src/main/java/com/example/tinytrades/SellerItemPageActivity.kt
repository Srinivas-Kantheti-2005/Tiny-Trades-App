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
import com.example.tinytrades.database.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SellerItemPageActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var profileDao: ProfileDao
    private lateinit var itemDao: ItemDao

    private lateinit var backbtn: ImageButton
    private lateinit var sellerImage: ImageView
    private lateinit var sellerName: TextView
    private lateinit var sellerItemPageRecyclerView: RecyclerView

    private lateinit var sellerItemPageRecyclerViewAdapter: SellerItemPageRecyclerViewAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_item_page)

        val username = intent.getStringExtra("USERNAME") ?: ""

        database = AppDatabase.getDatabase(applicationContext)
        userDao = database.userDao()
        profileDao = database.profileDao()
        itemDao = database.itemDao()

        backbtn = findViewById(R.id.backbtn)
        sellerImage = findViewById(R.id.sellerImage)
        sellerName = findViewById(R.id.sellername)
        sellerItemPageRecyclerView = findViewById(R.id.sellerItemPageRecyclerView)

        sellerItemPageRecyclerViewAdapter = SellerItemPageRecyclerViewAdapter(mutableListOf())
        sellerItemPageRecyclerView.adapter = sellerItemPageRecyclerViewAdapter
        sellerItemPageRecyclerView.layoutManager = GridLayoutManager(this, 3)

        loadSellerProfile(username)
        loadSellerItems(username)

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
                if(profile != null) {
                    populatedSellerProfile(profile)
                } else {
                    showToast("Profile not found")
                }
            } catch (e: Exception) {
                showToast("Error loading profile: ${e.message}")
            }
        }
    }

    private fun loadSellerItems(username: String) {
        lifecycleScope.launch {
            try {
                val sellerItems = withContext(Dispatchers.IO) {
                    itemDao.getItemByUsername(username)
                }
                sellerItemPageRecyclerViewAdapter.updateItems(sellerItems)
            } catch (e: Exception) {
                showToast("Error loading items: ${e.message}")
            }
        }
    }

    private fun populatedSellerProfile(profile: Profile) {
        sellerImage.setImageResource(profile.image)
        sellerName.text = "${profile.firstname} ${profile.lastname}"
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
