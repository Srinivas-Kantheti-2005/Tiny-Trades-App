package com.example.tinytrades

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class CreateAccount : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        val backbtn = findViewById<ImageButton>(R.id.backbtn)

        backbtn.setOnClickListener {
            val backbtn = Intent(this, ProfileActivity::class.java)
            startActivity(backbtn)
        }
    }
}