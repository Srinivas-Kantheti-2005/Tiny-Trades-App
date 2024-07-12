package com.example.tinytrades

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val backbtn = findViewById<ImageButton>(R.id.backbtn)
        val homebtn = findViewById<ImageButton>(R.id.home)
        val explorebtn = findViewById<ImageButton>(R.id.explore)

        explorebtn.setOnClickListener {
            val explorebtn = Intent(this, MainActivity::class.java).apply {
                putExtra("navigate_to", "explore")
            }
            startActivity(explorebtn)
        }

        homebtn.setOnClickListener {
            val home = Intent(this, MainActivity::class.java)
            startActivity(home)
        }

        backbtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
