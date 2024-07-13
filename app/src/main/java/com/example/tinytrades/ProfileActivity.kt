package com.example.tinytrades

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val backbtn = findViewById<ImageButton>(R.id.backbtn)
        val homebtn = findViewById<ImageButton>(R.id.home)
        val explorebtn = findViewById<ImageButton>(R.id.explore)
        val loginbtn = findViewById<Button>(R.id.loginpbtn)
        val newaccountbtn = findViewById<Button>(R.id.newaccount)

        backbtn.setOnClickListener {
            onBackPressed()
        }

        newaccountbtn.setOnClickListener {
            val createaccount = Intent(this, CreateAccount::class.java)
            startActivity(createaccount)
        }

        loginbtn.setOnClickListener {
            val login = Intent(this, LoginPage::class.java)
            startActivity(login)
        }

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
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
