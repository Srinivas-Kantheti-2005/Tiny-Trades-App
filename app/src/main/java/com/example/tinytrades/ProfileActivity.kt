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
import com.example.tinytrades.database.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var backbtn: ImageButton
    private lateinit var homebtn: ImageButton
    private lateinit var explorebtn: ImageButton
    private lateinit var sellbtn: ImageButton
    private lateinit var savebtn: Button
    private lateinit var loginbtn: Button
    private lateinit var newaccountbtn: Button
    private lateinit var username: EditText
    private lateinit var firstname: EditText
    private lateinit var lastname: EditText
    private lateinit var gender: EditText
    private lateinit var mobileno: EditText
    private lateinit var emailid: EditText
    private lateinit var dno: EditText
    private lateinit var street: EditText
    private lateinit var village: EditText
    private lateinit var pincode: EditText
    private lateinit var mandal: EditText
    private lateinit var district: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "tinytrades-database"
        ).build()

        backbtn = findViewById(R.id.backbtn)
        homebtn = findViewById(R.id.home)
        explorebtn = findViewById(R.id.explore)
        sellbtn = findViewById(R.id.sell)
        savebtn = findViewById(R.id.save)
        loginbtn = findViewById(R.id.loginpbtn)
        newaccountbtn = findViewById(R.id.newaccount)

        savebtn.setOnClickListener {
            getProfile()
        }

        sellbtn.setOnClickListener {
            val sellIntent = Intent(this, SellActivity::class.java)
            startActivity(sellIntent)
        }

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

    private fun getProfile() {
        username = findViewById(R.id.username)
        firstname = findViewById(R.id.firstname)
        lastname = findViewById(R.id.lastname)
        gender = findViewById(R.id.gender)
        mobileno = findViewById(R.id.mobileno)
        emailid = findViewById(R.id.emailid)
        dno = findViewById(R.id.dno)
        street = findViewById(R.id.street)
        village = findViewById(R.id.village)
        pincode = findViewById(R.id.pincode)
        mandal = findViewById(R.id.mandal)
        district = findViewById(R.id.district)

        val username = username.text.toString()
        val firstName = firstname.text.toString()
        val lastName = lastname.text.toString()
        val genderText = gender.text.toString()
        val mobileNo = mobileno.text.toString()
        val emailId = emailid.text.toString()
        val dNo = dno.text.toString()
        val streetText = street.text.toString()
        val villageText = village.text.toString()
        val pinCode = pincode.text.toString()
        val mandalText = mandal.text.toString()
        val districtText = district.text.toString()

        if (isValidProfile(
                firstName,
                lastName,
                genderText,
                mobileNo,
                emailId,
                username,
                dNo,
                streetText,
                villageText,
                pinCode,
                mandalText,
                districtText
            )
        ) {
            val profileImage = if (genderText.equals("male", ignoreCase = true)) {
                R.drawable.men
            } else {
                R.drawable.women
            }

            val newProfile = Profile(
                emailId = emailId,
                username = username,
                image = profileImage,
                firstname = firstName,
                lastname = lastName,
                gender = genderText,
                mobile = mobileNo.toInt(),
                dno = dNo,
                street = streetText,
                village = villageText,
                pinCode = pinCode.toInt(),
                mandal = mandalText,
                district = districtText
            )

            lifecycleScope.launch {
                val existingUser = withContext(Dispatchers.IO) {
                    database.userDao().getUserByUsername(username)

                }
                if(existingUser == null) {
                    showToastMsg("username not found")
                }
                else {
                    val existingProfile = withContext(Dispatchers.IO) {
                        database.profileDao().getProfileByEmailId(emailId)
                    }
                    if (existingProfile != null) {
                        showToastMsg("Profile already exists")
                    } else {
                        withContext(Dispatchers.IO) {
                            database.profileDao().insert(newProfile)
                        }
                        showToastMsg("Profile created successfully")
                    }
                }
            }
        }
    }

    private fun isValidProfile(
        firstName: String,
        lastName: String,
        genderText: String,
        mobileNo: String,
        emailId: String,
        username: String,
        dNo: String,
        streetText: String,
        villageText: String,
        pinCode: String,
        mandalText: String,
        districtText: String
    ): Boolean {
        return when {
            username.isEmpty() -> {
                showToastMsg("Fill the username field")
                false
            }

            firstName.isEmpty() -> {
                showToastMsg("Fill the first name field")
                false
            }

            lastName.isEmpty() -> {
                showToastMsg("Fill the last name field")
                false
            }

            genderText.isEmpty() -> {
                showToastMsg("Fill the gender field")
                false
            }

            mobileNo.isEmpty() -> {
                showToastMsg("Fill the mobile no field")
                false
            }

            emailId.isEmpty() -> {
                showToastMsg("Fill the email id field")
                false
            }

            dNo.isEmpty() -> {
                showToastMsg("Fill the dno field")
                false
            }

            streetText.isEmpty() -> {
                showToastMsg("Fill the street field")
                false
            }

            villageText.isEmpty() -> {
                showToastMsg("Fill the village field")
                false
            }

            pinCode.isEmpty() -> {
                showToastMsg("Fill the pin code field")
                false
            }

            mandalText.isEmpty() -> {
                showToastMsg("Fill the mandal field")
                false
            }

            districtText.isEmpty() -> {
                showToastMsg("Fill the district field")
                false
            }

            else -> true
        }
    }

    private fun showToastMsg(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
