package com.example.tinytrades

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ItemDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)

        val backbtn = findViewById<ImageButton>(R.id.backbtn)
        val itemImage: ImageView = findViewById(R.id.itemImage)
        val itemTitle: TextView = findViewById(R.id.itemTitle)
        val itemSize: TextView = findViewById(R.id.itemSize)
        val itemPrice: TextView = findViewById(R.id.itemPrice)

        backbtn.setOnClickListener {
            onBackPressed()
        }

        val bundle = intent.extras
        if (bundle != null) {
            itemImage.setImageResource(bundle.getInt("itemImage"))
            itemTitle.text = bundle.getString("itemTitle")
            itemSize.text = bundle.getString("itemSize")
            itemPrice.text = bundle.getString("itemPrice")
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        finish()
    }
}
