package com.example.tinytrades

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tinytrades.database.AppDatabase
import com.example.tinytrades.database.ItemDao
import com.example.tinytrades.database.ProfileDao
import com.example.tinytrades.database.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ItemDetailsActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var profileDao: ProfileDao
    private lateinit var itemDao: ItemDao

    private lateinit var backbtn: ImageButton
    private lateinit var addtocartbtn: Button
    private lateinit var buynow: Button

    private lateinit var image: ImageView
    private lateinit var title: TextView
    private lateinit var size: TextView
    private lateinit var price: TextView
    private lateinit var sellername: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)

        database = AppDatabase.getDatabase(applicationContext)
        userDao = database.userDao()
        profileDao = database.profileDao()
        itemDao = database.itemDao()

        backbtn = findViewById(R.id.backbtn)
        addtocartbtn = findViewById(R.id.addtocart)
        buynow = findViewById(R.id.buynow)

        image = findViewById(R.id.itemImage)
        title = findViewById(R.id.itemTitle)
        size = findViewById(R.id.itemSize)
        price = findViewById(R.id.itemPrice)
        sellername = findViewById(R.id.sellername)

        backbtn.setOnClickListener {
            onBackPressed()
        }

        val bundle = intent.extras
        if(bundle != null) {
            val itemImageResId = bundle.getByteArray("itemImage")
            val itemTitleText = bundle.getString("itemTitle", "")
            val itemSizeText = bundle.getString("itemSize", "")
            val itemPriceText = bundle.getDouble("itemPrice", 0.0)
            val sellerUsername = bundle.getString("sellerUsername","")

            itemImageResId?.let {
                image.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
            }
            title.text = itemTitleText
            size.text = itemSizeText
            price.text = itemPriceText.toString()

            GlobalScope.launch(Dispatchers.Main) {
                val profile = with(Dispatchers.IO) {
                    profileDao.getProfileByUsername(sellerUsername)
                }
                sellername.text = profile?.firstname ?: "Unknown Seller"

                sellername.setOnClickListener {
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
}
