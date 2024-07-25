package com.example.tinytrades

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tinytrades.database.AppDatabase
import com.example.tinytrades.database.CartDao
import com.example.tinytrades.database.ItemDao
import com.example.tinytrades.database.ProfileDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ItemDetailsActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var profileDao: ProfileDao
    private lateinit var itemDao: ItemDao
    private lateinit var cartDao: CartDao

    private lateinit var backbtn: ImageButton
    private lateinit var buynow: Button

    private lateinit var buyerUsername: EditText
    private lateinit var image: ImageView
    private lateinit var title: TextView
    private lateinit var size: TextView
    private lateinit var price: TextView
    private lateinit var sellerName: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)

        database = AppDatabase.getDatabase(applicationContext)
        profileDao = database.profileDao()
        itemDao = database.itemDao()
        cartDao = database.cartDao()

        backbtn = findViewById(R.id.backbtn)
        buynow = findViewById(R.id.buynow)

        buyerUsername = findViewById(R.id.buyerUsername)
        image = findViewById(R.id.itemImage)
        title = findViewById(R.id.itemTitle)
        size = findViewById(R.id.itemSize)
        price = findViewById(R.id.itemPrice)
        sellerName = findViewById(R.id.username)

        backbtn.setOnClickListener {
            onBackPressed()
        }

        // Removed the addtocartbtn listener

        val bundle = intent.extras
        if (bundle != null) {
            val itemImageResId = bundle.getByteArray("itemImage")
            val itemTitleText = bundle.getString("itemTitle", "")
            val itemSizeText = bundle.getString("itemSize", "")
            val itemPriceText = bundle.getDouble("itemPrice", 0.0)
            val sellerUsername = bundle.getString("sellerUsername", "")
            val buyerUserName = bundle.getString("buyerUsername", "")

            itemImageResId?.let {
                image.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
            }
            title.text = itemTitleText
            size.text = itemSizeText
            price.text = itemPriceText.toString()
            buyerUsername.setText(buyerUserName)

            GlobalScope.launch(Dispatchers.Main) {
                val profile = withContext(Dispatchers.IO) {
                    profileDao.getProfileByUsername(sellerUsername)
                }
                sellerName.text = profile?.firstname ?: "Unknown Seller"

                sellerName.setOnClickListener {
                    val profileIntent = Intent(this@ItemDetailsActivity, SellerProfileActivity::class.java).apply {
                        putExtra("USERNAME", sellerUsername)
                    }
                    startActivity(profileIntent)
                }
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
