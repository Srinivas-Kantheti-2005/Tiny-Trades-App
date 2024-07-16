package com.example.tinytrades

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.tinytrades.database.AppDatabase
import com.example.tinytrades.database.Item
import com.example.tinytrades.database.ItemDao
import com.example.tinytrades.database.ProfileDao
import com.example.tinytrades.database.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class SellActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var profileDao: ProfileDao
    private lateinit var itemDao: ItemDao

    private lateinit var backbtn: ImageButton
    private lateinit var takepicture: Button
    private lateinit var gallery: Button
    private lateinit var save: Button
    private lateinit var delete: Button

    private lateinit var image: ImageView
    private lateinit var title: EditText
    private lateinit var quantity: EditText
    private lateinit var username: EditText
    private lateinit var price: EditText
    private lateinit var emailId: EditText
    private lateinit var size: EditText

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_CAMERA_PERMISSION = 2
    private val REQUEST_IMAGE_GALLERY = 3

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell)

        database = AppDatabase.getDatabase(applicationContext)
        userDao = database.userDao()
        profileDao = database.profileDao()
        itemDao = database.itemDao()

        backbtn = findViewById(R.id.backbtn)
        takepicture = findViewById(R.id.takepicture)
        gallery = findViewById(R.id.gallery)
        save = findViewById(R.id.save)
        delete = findViewById(R.id.delete)

        image = findViewById(R.id.image)
        title = findViewById(R.id.title)
        quantity = findViewById(R.id.quantity)
        username = findViewById(R.id.username)
        price = findViewById(R.id.price)
        emailId = findViewById(R.id.emailid)
        size = findViewById(R.id.size)

        backbtn.setOnClickListener {
            onBackPressed()
        }

        save.setOnClickListener {
            saveItem()
        }

        takepicture.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION
                )
            } else {
                dispatchTakePictureIntent()
            }
        }

        gallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
        }
    }

    private fun saveItem() {
        val imageDrawable = image.drawable
        val itemImage: ByteArray? = if (imageDrawable != null) {
            val bitmap = (imageDrawable as BitmapDrawable).bitmap
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            byteArrayOutputStream.toByteArray()
        } else {
            null
        }

        val titleText = title.text.toString()
        val quantityText = quantity.text.toString()
        val userName = username.text.toString()
        val priceText = price.text.toString()
        val emailIdText = emailId.text.toString()
        val sizeText = size.text.toString()

        if (validateFields(itemImage, titleText, quantityText, userName, priceText, emailIdText, sizeText)) {
            lifecycleScope.launch {
                try {
                    val existingUser = withContext(Dispatchers.IO) {
                        database.userDao().getUserByUsername(userName)
                    }
                    val existingProfile = withContext(Dispatchers.IO) {
                        database.profileDao().getProfileByEmailId(emailIdText)
                    }

                    if (existingUser != null && existingProfile != null) {
                        val existingItem = withContext(Dispatchers.IO) {
                            database.itemDao().getItemByTitle(titleText)
                        }

                        if (existingItem == null) {
                            val newItem = Item(
                                image = itemImage,
                                title = titleText,
                                quantity = quantityText.toInt(),
                                username = userName,
                                price = priceText.toDouble(),
                                emailid = emailIdText,
                                size = sizeText
                            )
                            withContext(Dispatchers.IO) {
                                database.itemDao().insert(newItem)
                            }
                            showToastMsg("Item added successfully")
                            clearFields()
                            finish()
                        } else {
                            showToastMsg("Item with title already exists")
                        }
                    } else {
                        showToastMsg("User or profile not found")
                    }
                } catch (e: Exception) {
                    showToastMsg("Error: ${e.message}")
                }
            }
        }
    }


    private fun validateFields(
        itemImage: ByteArray?,
        titleText: String,
        quantityText: String,
        userName: String,
        priceText: String,
        emailIdText: String,
        sizeText: String
    ): Boolean {
        return when {
            itemImage == null -> {
                showToastMsg("Please upload an image")
                false
            }
            titleText.isEmpty() -> {
                showToastMsg("Please fill the title field")
                false
            }
            quantityText.isEmpty() -> {
                showToastMsg("Please fill the quantity field")
                false
            }
            userName.isEmpty() -> {
                showToastMsg("Please fill the username field")
                false
            }
            priceText.isEmpty() -> {
                showToastMsg("Please fill the price field")
                false
            }
            emailIdText.isEmpty() -> {
                showToastMsg("Please fill the email id field")
                false
            }
            sizeText.isEmpty() -> {
                showToastMsg("Please fill the size field")
                false
            }
            else -> true
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } ?: run {
                Toast.makeText(this, "Camera not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    image.setImageBitmap(imageBitmap)
                }
                REQUEST_IMAGE_GALLERY -> {
                    val selectedImageUri: Uri? = data?.data
                    selectedImageUri?.let {
                        val bitmap =
                            MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                        image.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }

    private fun showToastMsg(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun clearFields() {
        image.setImageDrawable(null)
        title.text.clear()
        quantity.text.clear()
        username.text.clear()
        price.text.clear()
        emailId.text.clear()
        size.text.clear()
    }
}
