package com.example.tinytrades

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
import com.example.tinytrades.database.User
import com.example.tinytrades.database.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateAccount : AppCompatActivity() {

    private lateinit var userDao: UserDao
    private lateinit var profileDao: ProfileDao
    private lateinit var itemDao: ItemDao
    private lateinit var database: AppDatabase

    private lateinit var createUsername: EditText
    private lateinit var createPassword: EditText
    private lateinit var confirmPassword: EditText

    private lateinit var backbtn: ImageButton
    private lateinit var createbtn: Button
    private lateinit var deletebtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "tinytrades-database"
        ).build()

        userDao = database.userDao()
        profileDao = database.profileDao()
        itemDao = database.itemDao()

        createUsername = findViewById(R.id.createusername)
        createPassword = findViewById(R.id.createpassword)
        confirmPassword = findViewById(R.id.confirmpassword)

        backbtn = findViewById(R.id.backbtn)
        createbtn = findViewById(R.id.create)
        deletebtn = findViewById(R.id.delete)

        backbtn.setOnClickListener {
            onBackPressed()
        }

        createbtn.setOnClickListener {
            getUser()
        }

        deletebtn.setOnClickListener {
            deleteUser()
        }
    }

    private fun deleteUser() {
        val username = createUsername.text.toString()
//        val password = createPassword.text.toString()
//        val cpassword = confirmPassword.text.toString()

        if (username.isEmpty()) {
            showToastMsg("Fill the username field")
            return
        }
        lifecycleScope.launch {
            val userDelete = withContext(Dispatchers.IO) {
                database.userDao().getUserByUsername(username)
            }
            if (userDelete != null) {
                withContext(Dispatchers.IO) {
                    database.userDao().delete(userDelete)
                }
                showToastMsg("Credentials deleted successfully")
            } else {
                showToastMsg("User not found")
            }
            runOnUiThread {
                clearFields()
            }
        }
    }

    private fun getUser() {
        val username = createUsername.text.toString()
        val password = createPassword.text.toString()
        val cpassword = confirmPassword.text.toString()

        isValidUser(username, password, cpassword)
    }

    private fun isValidUser(
        username: String,
        password: String,
        cpassword: String
    ) {
        when {
            username.isEmpty() -> {
                showToastMsg("Fill the create username field")
            }

            password.isEmpty() -> {
                showToastMsg("Fill the password field")
            }

            !isValidLength(password) -> {
                showToastMsg("Password length must be between 4 to 20 characters")
            }

            !hasUpperCase(password) -> {
                showToastMsg("Password must contain at least one uppercase letter")
            }

            !hasLowerCase(password) -> {
                showToastMsg("Password must contain at least one lowercase letter")
            }

            !hasDigits(password) -> {
                showToastMsg("Password must contain at least one digit")
            }

            !hasSpecialCharacter(password) -> {
                showToastMsg("Password must contain at least one special character")
            }

            cpassword.isEmpty() -> {
                showToastMsg("Fill the confirm password field")
            }

            password != cpassword -> {
                showToastMsg("Password and confirm password fields must be the same")
            }

            else -> {
                lifecycleScope.launch {
                    val existingUsers = withContext(Dispatchers.IO) {
                        database.userDao().getUserByUsername(username)
                    }
                    if (existingUsers != null) {
                        runOnUiThread {
                            showToastMsg("Username already exists")
                        }
                    } else {
                        val newUser = User(username, password)
                        withContext(Dispatchers.IO) {
                            database.userDao().insertUser(newUser)
                        }
                        runOnUiThread {
                            showToastMsg("Account created successfully")
                            clearFields()
                        }
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

    private fun hasLowerCase(password: String): Boolean {
        return password.any { it.isLowerCase() }
    }

    private fun hasDigits(password: String): Boolean {
        return password.any { it.isDigit() }
    }

    private fun hasSpecialCharacter(password: String): Boolean {
        val specialCharacters = "!@#$%^&*()-_=+[]{}|;:/,.?<>`~"
        return password.any { it in specialCharacters }
    }

    private fun clearFields() {
        createUsername.text.clear()
        createPassword.text.clear()
        confirmPassword.text.clear()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
