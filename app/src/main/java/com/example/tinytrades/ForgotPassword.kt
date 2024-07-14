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

class ForgotPassword : AppCompatActivity() {

    private lateinit var userDao: UserDao
    private lateinit var profileDao: ProfileDao
    private lateinit var itemDao: ItemDao
    private lateinit var database: AppDatabase

    private lateinit var backbtn: ImageButton
    private lateinit var changebtn: Button

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var cpassword: EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "tinytrades-database"
        ).build()

        userDao = database.userDao()
        profileDao = database.profileDao()
        itemDao = database.itemDao()

        username = findViewById(R.id.Username)
        password = findViewById(R.id.newpin)
        cpassword = findViewById(R.id.cpin)

        backbtn = findViewById(R.id.backbtn)
        changebtn = findViewById(R.id.changebtn)

        backbtn.setOnClickListener {
            val backbtn = Intent(this, LoginPage::class.java)
            startActivity(backbtn)
        }

        changebtn.setOnClickListener {
            chage()
        }
    }

    private fun chage() {
        val userName = username.text.toString()
        val passWord = password.text.toString()
        val cpassWord = cpassword.text.toString()

        if (isValidCredentials(userName, passWord, cpassWord)) {
            lifecycleScope.launch {
                val existingUser = withContext(Dispatchers.IO) {
                    database.userDao().getUserByUsername(userName)
                }
                if(existingUser == null) {
                    showToastMsg("account not found")
                }
                else {
                    existingUser.password = passWord
                    withContext(Dispatchers.IO) {
                        userDao.update(existingUser)
                    }
                    runOnUiThread {
                        showToastMsg("password updated successfully")
                        clearFields()
                        val changeIntent = Intent(this@ForgotPassword, LoginPage::class.java)
                        startActivity(changeIntent)
                        finish()
                    }
                }
            }
        }
    }

    private fun isValidCredentials(userName: String, passWord: String, cpassWord: String): Boolean {
        return when {
            userName.isEmpty() -> {
                showToastMsg("Fill the username field")
                false
            }

            passWord.isEmpty() -> {
                showToastMsg("Fill the new password field")
                false
            }

            !isValidLength(passWord) -> {
                showToastMsg("password length must be between 4 to 20 characters")
                false
            }

            !hasUpperCase(passWord) -> {
                showToastMsg("password contains at least one upper case character")
                false
            }

            !hasLowerCase(passWord) -> {
                showToastMsg("password contains at least one lower case character")
                false
            }

            !hasDigit(passWord) -> {
                showToastMsg("password contains at least one digit")
                false
            }

            !hasSpecialCharacter(passWord) -> {
                showToastMsg("password contains at least one special character")
                false
            }

            cpassWord.isEmpty() -> {
                showToastMsg("Fill the confirm password field")
                false
            }

            passWord != cpassWord -> {
                showToastMsg("passwords are mismatched")
                false
            }

            else -> true
        }
    }
    private fun isValidLength(passWord: String): Boolean {
        return passWord.length in 4..20
    }

    private fun hasUpperCase(passWord: String): Boolean {
        return passWord.any { it.isUpperCase() }
    }

    private fun hasLowerCase(passWord: String): Boolean {
        return passWord.any { it.isLowerCase() }
    }

    private fun hasDigit(passWord: String): Boolean {
        return passWord.any { it.isDigit() }
    }

    private fun hasSpecialCharacter(password: String): Boolean {
        val specialCharacters = "!@#$%^&*()-_=+[]{}|;:/,.?<>`~"
        return password.any { it in specialCharacters }
    }

    private fun showToastMsg(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun clearFields() {
        username.text.clear()
        password.text.clear()
        cpassword.text.clear()
    }
}
