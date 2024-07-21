package com.example.tinytrades

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.tinytrades.database.AppDatabase
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

    private lateinit var cartImage: ImageView
    private lateinit var cartTitle: TextView
    private lateinit var cartSize: TextView
    private lateinit var decrement: ImageButton
    private lateinit var quantity: EditText
    private lateinit var increment: ImageButton
    private lateinit var price: TextView

    private lateinit var backbtn: ImageButton
    private lateinit var buyerImage: ImageView
    private lateinit var buyerName: TextView
    private lateinit var add_to_cart_recycler_view: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_cart)

        val username = intent.getStringExtra("USERNAME") ?: ""

        database = AppDatabase.getDatabase(applicationContext)
        userDao = database.userDao()
        profileDao = database.profileDao()
        itemDao = database.itemDao()

        if(username.isNotEmpty()) {
            loadProfile(username)
        }

        backbtn = findViewById(R.id.backbtn)
        buyerImage = findViewById(R.id.buyerImage)
        buyerName = findViewById(R.id.buyerName)

        backbtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loadProfile(username: String) {
        lifecycleScope.launch {
            try {
                val profile = withContext(Dispatchers.IO) {
                    profileDao.getProfileByUsername(username)
                }
                if(profile != null) {
                    populatedBuyerProfile(profile)
                }
                else {
                    showToast("profile not found")
                }
            } catch(e: Exception) {
                showToast("Error loaded profile: ${e.message}")
            }
        }
    }

    private fun populatedBuyerProfile(profile: Profile) {
        buyerImage.setImageResource(profile.image)
        buyerName.text = "${profile.firstname} ${profile.lastname}"
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message,Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
