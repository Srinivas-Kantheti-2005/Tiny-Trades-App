package com.example.tinytrades

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ItemDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)

        val itemImage: ImageView = findViewById(R.id.itemImage)
        val itemTitle: TextView = findViewById(R.id.itemTitle)
        val itemPrice: TextView = findViewById(R.id.itemPrice)

        val bundle = intent.extras
        if (bundle != null) {
            itemImage.setImageResource(bundle.getInt("itemImage"))
            itemTitle.text = bundle.getString("itemTitle")
            itemPrice.text = bundle.getString("itemPrice")
        }
    }
}
