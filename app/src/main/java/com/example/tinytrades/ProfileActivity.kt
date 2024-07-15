package com.example.tinytrades

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.tinytrades.database.AppDatabase
import com.example.tinytrades.database.Profile
import com.example.tinytrades.database.ProfileDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileDao: ProfileDao
    private lateinit var database: AppDatabase

    private lateinit var backbtn: ImageButton
    private lateinit var homebtn: ImageButton
    private lateinit var explorebtn: ImageButton
    private lateinit var sellbtn: ImageButton

    private lateinit var savebtn: Button
    private lateinit var deletebtn: Button
    private lateinit var updatebtn: Button
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
    private lateinit var profileImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "tinytrades-database"
        )
            .fallbackToDestructiveMigration()
            .build()

        profileDao = database.profileDao()

        backbtn = findViewById(R.id.backbtn)
        homebtn = findViewById(R.id.home)
        explorebtn = findViewById(R.id.explore)
        sellbtn = findViewById(R.id.sell)

        savebtn = findViewById(R.id.save)
        deletebtn = findViewById(R.id.delete)
        updatebtn = findViewById(R.id.update)
        loginbtn = findViewById(R.id.loginpbtn)
        newaccountbtn = findViewById(R.id.newaccount)

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
        profileImage = findViewById(R.id.profileimage)

        val usernameExtra = intent.getStringExtra("USERNAME") ?: ""

        lifecycleScope.launch {
            val profile = withContext(Dispatchers.IO) {
                profileDao.getProfileByUsername(usernameExtra)
            }

            if (profile != null) {
                populateFields(profile)
            } else {
                showToast("Profile not found")
            }
        }

        savebtn.setOnClickListener {
            saveProfile()
        }

        deletebtn.setOnClickListener {
            deleteProfile()
        }

        updatebtn.setOnClickListener {
            updateProfile()
        }

        sellbtn.setOnClickListener {
            val sellIntent = Intent(this, SellActivity::class.java)
            startActivity(sellIntent)
        }

        backbtn.setOnClickListener {
            onBackPressed()
        }

        newaccountbtn.setOnClickListener {
            val createAccountIntent = Intent(this, CreateAccount::class.java)
            startActivity(createAccountIntent)
        }

        loginbtn.setOnClickListener {
            val loginIntent = Intent(this, LoginPage::class.java)
            startActivity(loginIntent)
        }

        explorebtn.setOnClickListener {
            val exploreIntent = Intent(this, MainActivity::class.java).apply {
                putExtra("navigate_to", "explore")
            }
            startActivity(exploreIntent)
        }

        homebtn.setOnClickListener {
            val homeIntent = Intent(this, MainActivity::class.java)
            startActivity(homeIntent)
        }
    }

    private fun populateFields(profile: Profile) {
        username.setText(profile.username)
        firstname.setText(profile.firstname)
        lastname.setText(profile.lastname)
        gender.setText(profile.gender)
        mobileno.setText(profile.mobile.toString())
        emailid.setText(profile.emailId)
        dno.setText(profile.dno)
        street.setText(profile.street)
        village.setText(profile.village)
        pincode.setText(profile.pinCode.toString())
        mandal.setText(profile.mandal)
        district.setText(profile.district)

        val profileImageResource = if (profile.gender.equals("male", ignoreCase = true)) {
            R.drawable.men
        } else {
            R.drawable.women
        }
        profileImage.setImageResource(profileImageResource)
    }

    private fun saveProfile() {
        val userName = username.text.toString()
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

        if (isValidProfile(firstName, lastName, genderText, mobileNo, emailId, userName, dNo, streetText, villageText, pinCode, mandalText, districtText)) {
            val profileImage = if (genderText.equals("male", ignoreCase = true)) {
                R.drawable.men
            } else {
                R.drawable.women
            }

            lifecycleScope.launch {
                val existingProfile = withContext(Dispatchers.IO) {
                    profileDao.getProfileByUsername(userName)
                }

                if (existingProfile == null) {
                    val newProfile = Profile(
                        username = userName,
                        emailId = emailId,
                        firstname = firstName,
                        lastname = lastName,
                        gender = genderText,
                        mobile = mobileNo.toLong(),
                        dno = dNo,
                        street = streetText,
                        village = villageText,
                        pinCode = pinCode.toLong(),
                        mandal = mandalText,
                        district = districtText,
                        image = profileImage
                    )

                    withContext(Dispatchers.IO) {
                        profileDao.insert(newProfile)
                    }
                    showToast("Profile created successfully")
                } else {
                    showToast("Profile already exists")
                }
            }
        }
    }


    private fun deleteProfile() {
        val emailIdValue = emailid.text.toString()
        if (emailIdValue.isEmpty()) {
            showToast("Enter email id")
            return
        }
        lifecycleScope.launch {
            val profile = withContext(Dispatchers.IO) {
                profileDao.getProfileByEmailId(emailIdValue)
            }
            if (profile != null && profile.emailId.isNotEmpty()) {
                withContext(Dispatchers.IO) {
                    profileDao.delete(profile)
                }
                showToast("Profile deleted successfully")
                clearFields()
            } else {
                showToast("Profile not found")
            }
        }
    }

    private fun updateProfile() {
        val userName = username.text.toString()
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

        if (isValidProfile(firstName, lastName, genderText, mobileNo, emailId, userName, dNo, streetText, villageText, pinCode, mandalText, districtText)) {
            val profileImage = if (genderText.equals("male", ignoreCase = true)) {
                R.drawable.men
            } else {
                R.drawable.women
            }

            lifecycleScope.launch {
                val existingProfile = withContext(Dispatchers.IO) {
                    profileDao.getProfileByEmailId(emailId)
                }

                if (existingProfile == null) {
                    showToast("Profile not found")
                } else {
                    val updatedProfile = existingProfile.copy(
                        username = userName,
                        emailId = emailId,
                        firstname = firstName,
                        lastname = lastName,
                        gender = genderText,
                        mobile = mobileNo.toLong(),
                        dno = dNo,
                        street = streetText,
                        village = villageText,
                        pinCode = pinCode.toLong(),
                        mandal = mandalText,
                        district = districtText,
                        image = profileImage
                    )

                    withContext(Dispatchers.IO) {
                        profileDao.update(updatedProfile)
                    }
                    showToast("Profile updated successfully")
                }
            }
        }
    }

    private fun isValidProfile(
        firstName: String,
        lastName: String,
        gender: String,
        mobileNo: String,
        emailId: String,
        userName: String,
        dNo: String,
        street: String,
        village: String,
        pinCode: String,
        mandal: String,
        district: String
    ): Boolean {
        return when {
            firstName.isEmpty() -> {
                showToast("Enter first name")
                false
            }
            lastName.isEmpty() -> {
                showToast("Enter last name")
                false
            }
            gender.isEmpty() -> {
                showToast("Enter gender")
                false
            }
            mobileNo.isEmpty() -> {
                showToast("Enter mobile number")
                false
            }
            emailId.isEmpty() -> {
                showToast("Enter email id")
                false
            }
            userName.isEmpty() -> {
                showToast("Enter username")
                false
            }
            dNo.isEmpty() -> {
                showToast("Enter door number")
                false
            }
            street.isEmpty() -> {
                showToast("Enter street")
                false
            }
            village.isEmpty() -> {
                showToast("Enter village")
                false
            }
            pinCode.isEmpty() -> {
                showToast("Enter pin code")
                false
            }
            mandal.isEmpty() -> {
                showToast("Enter mandal")
                false
            }
            district.isEmpty() -> {
                showToast("Enter district")
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
        firstname.text.clear()
        lastname.text.clear()
        gender.text.clear()
        mobileno.text.clear()
        emailid.text.clear()
        dno.text.clear()
        street.text.clear()
        village.text.clear()
        pincode.text.clear()
        mandal.text.clear()
        district.text.clear()
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        finish()
    }
}
