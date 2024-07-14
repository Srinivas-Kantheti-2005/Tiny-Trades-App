package com.example.tinytrades

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
import com.example.tinytrades.database.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateAccount : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var createUsername: EditText
    private lateinit var createPassword: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var backbtn: ImageButton
    private lateinit var createbtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "tinytrades-database"
        ).build()

        createbtn = findViewById(R.id.create)
        backbtn = findViewById(R.id.backbtn)

        createbtn.setOnClickListener {
            getUser()
        }

        backbtn.setOnClickListener {
            val backbtn = Intent(this, ProfileActivity::class.java)
            startActivity(backbtn)
        }
    }

    private fun getUser() {
        createUsername = findViewById(R.id.createusername)
        createPassword = findViewById(R.id.createpassword)
        confirmPassword = findViewById(R.id.confirmpassword)

        val username = createUsername.text.toString()
        val password = createPassword.text.toString()
        val cpassword = confirmPassword.text.toString()

        isValidUser(username, password, cpassword, createUsername, createPassword, confirmPassword)
    }

    private fun isValidUser(
        username: String,
        password: String,
        cpassword: String,
        createUsername: EditText,
        createPassword: EditText,
        confirmPassword: EditText
    ) {
        when {
            username.isEmpty() -> {
                showToastMsg("Fill the create username filed")
            }

            password.isEmpty() -> {
                showToastMsg("Fill the password username filed")
            }

            !isValidLength(password) -> {
                showToastMsg("password length must be between 4 to 20 characters")
            }

            !hasUpperCase(password) -> {
                showToastMsg("password must contains at least one uppercase letter")
            }

            !hasLowerCase(password) -> {
                showToastMsg("password must contains at least one lowercase letter")
            }

            !hasDigits(password) -> {
                showToastMsg("password must contains at least one digit")
            }

            !hasSpecialCharacter(password) -> {
                showToastMsg("password must contains at least one special character")
            }

            cpassword.isEmpty() -> {
                showToastMsg("Fill the confirm password filed")
            }

            password != cpassword -> {
                showToastMsg("password and confirm password fields must be same")
            }

            else -> {
                lifecycleScope.launch {
                    val existingUsers = withContext(Dispatchers.IO) {
                        database.userDao().getUserByUsername(username)
                    }
                    if(existingUsers != null) {
                        runOnUiThread {
                            showToastMsg("username already exist")
                        }
                    }
                    else {
                        val newUser = User(username, password)
                        withContext(Dispatchers.IO) {
                            database.userDao().insertUser(newUser)
                        }
                    }

                    runOnUiThread {
                        showToastMsg("Account created successfully")

                        createUsername.text.clear()
                        createPassword.text.clear()
                        confirmPassword.text.clear()
                    }
                }
            }
        }
    }

    private fun showToastMsg(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isValidLength(password: String): Boolean {
        return password.length in 4..20
    }

    private fun hasUpperCase(password: String): Boolean {
        return password.any { it.isUpperCase() }
    }

    private fun hasDigits(password: String): Boolean {
        return password.any { it.isDigit() }
    }

    private fun hasLowerCase(password: String): Boolean {
        return password.any { it.isLowerCase() }
    }

    private fun hasSpecialCharacter(password: String): Boolean {
        val sepcialCharacters = "!@#$%^&*()-_=+[]{}|;:/,.?<>`~"
        return password.any { it in sepcialCharacters }
    }
}
