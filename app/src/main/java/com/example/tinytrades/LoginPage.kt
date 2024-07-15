package com.example.tinytrades

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.tinytrades.database.AppDatabase
import com.example.tinytrades.database.ItemDao
import com.example.tinytrades.database.ProfileDao
import com.example.tinytrades.database.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginPage : AppCompatActivity() {

    private lateinit var userDao: UserDao
    private lateinit var profileDao: ProfileDao
    private lateinit var itemDao: ItemDao
    private lateinit var database: AppDatabase

    private lateinit var backbtn: ImageButton
    private lateinit var forgotpasswordbtn: Button
    private lateinit var loginbtn: Button

    private lateinit var username: EditText
    private lateinit var emailID: EditText
    private lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "tinytrades-database"
        )
            .fallbackToDestructiveMigration()
            .build()

        userDao = database.userDao()
        profileDao = database.profileDao()
        itemDao = database.itemDao()

        backbtn = findViewById(R.id.backbtn)
        forgotpasswordbtn = findViewById(R.id.forgotpassword)
        loginbtn = findViewById(R.id.loginbtn)

        backbtn.setOnClickListener {
            val backbtn = Intent(this, ProfileActivity::class.java)
            startActivity(backbtn)
        }

        forgotpasswordbtn.setOnClickListener {
            val forgotpassword = Intent(this, ForgotPassword::class.java)
            startActivity(forgotpassword)
        }

        loginbtn.setOnClickListener {
            login()
        }
    }

    private fun login() {
        username = findViewById(R.id.username)
        emailID = findViewById(R.id.emailid)
        password = findViewById(R.id.password)

        val userName = username.text.toString()
        val emaiLID = emailID.text.toString()
        val passWord = password.text.toString()

        if (isValidCredentials(userName, passWord)) {
            lifecycleScope.launch {
                val user = withContext(Dispatchers.IO) {
                    userDao.getUserByUsername(userName)
                }
                val profile = withContext(Dispatchers.IO) {
                    profileDao.getProfileByUsername(userName)
                }

                if (user == null) {
                    showToast("Account not found")
                } else if (user.password != passWord) {
                    showToast("Invalid password")
                } else {
                    if(profile != null) {
                        showToast("Login successful")
                        clearFields()
                        val profileIntent = Intent(this@LoginPage, ProfileActivity::class.java).apply {
                            putExtra("USERNAME", userName)
                        }
                        startActivity(profileIntent)
                        finish()
                    }
                    else {
                        showToast("account not found")
                    }
                }
            }
        }
    }

    private fun isValidCredentials(userName: String, passWord: String): Boolean {
        return when {
            userName.isEmpty() -> {
                showToast("Enter username")
                false
            }
            passWord.isEmpty() -> {
                showToast("Enter password")
                false
            }
            else -> true
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun clearFields() {
        username.text.clear()
        emailID.text.clear()
        password.text.clear()
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        finish()
    }
}
