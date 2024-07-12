package com.example.tinytrades

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ItemDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)

        val itemImage: ImageView = findViewById(R.id.itemImage)
        val itemTitle: TextView = findViewById(R.id.itemTitle)
        val itemSize: TextView = findViewById(R.id.itemSize)
        val itemPrice: TextView = findViewById(R.id.itemPrice)
        val sellername: TextView = findViewById(R.id.sellername)
        val backbtn = findViewById<ImageButton>(R.id.backbtn)

//        sellername.setOnClickListener {
//            val intent = Intent(this, SellerProfileActivity::class.java)
//            startActivity(intent)
//        }

        backbtn.setOnClickListener {
            onBackPressed()
        }

        val bundle = intent.extras
        if (bundle != null && bundle.containsKey("itemImage")) {
            val itemImageResId = bundle.getInt("itemImage")
            val itemTitleText = bundle.getString("itemTitle", "")
            val itemSizeText = bundle.getString("itemSize", "")
            val itemPriceText = bundle.getString("itemPrice", "")

            itemImage.setImageResource(itemImageResId)
            itemTitle.text = itemTitleText
            itemSize.text = itemSizeText
            itemPrice.text = itemPriceText
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        finish()
    }
}
