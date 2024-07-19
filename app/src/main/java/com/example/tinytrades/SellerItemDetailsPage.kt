package com.example.tinytrades

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.example.tinytrades.database.AppDatabase
import com.example.tinytrades.database.ItemDao
import com.example.tinytrades.database.ProfileDao
import com.example.tinytrades.database.UserDao
import java.io.ByteArrayOutputStream

class SellerItemDetailsPage : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var profileDao: ProfileDao
    private lateinit var itemDao: ItemDao

    private lateinit var backbtn: ImageButton
    private lateinit var takePicture: Button
    private lateinit var gallery: Button
    private lateinit var update: Button
    private lateinit var delete: Button

    private lateinit var itemImage: ImageView
    private lateinit var itemTitle: EditText
    private lateinit var itemQuantity: EditText
    private lateinit var itemUsername: EditText
    private lateinit var itemPrice: EditText
    private lateinit var emailId: EditText
    private lateinit var itemSize: EditText

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_CAMERA_PERMISSION = 2
    private val REQUEST_IMAGE_GALLERY = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_item_details_page)

        val usernameExtra = intent.getStringExtra("USERNAME") ?: ""

        database = AppDatabase.getDatabase(applicationContext)
        userDao = database.userDao()
        profileDao = database.profileDao()
        itemDao = database.itemDao()

        backbtn = findViewById(R.id.backbtn)
        takePicture = findViewById(R.id.takepicture)
        gallery = findViewById(R.id.gallery)
        update = findViewById(R.id.update)
        delete = findViewById(R.id.delete)

        itemImage = findViewById(R.id.image)
        itemTitle = findViewById(R.id.title)
        itemQuantity = findViewById(R.id.quantity)
        itemUsername = findViewById(R.id.username)
        itemPrice = findViewById(R.id.price)
        emailId = findViewById(R.id.emailid)
        itemSize = findViewById(R.id.size)

        backbtn.setOnClickListener {
            val backIntent = Intent(this, SellerItemPageActivity::class.java).apply {
                putExtra("USERNAME", usernameExtra)
            }
            startActivity(backIntent)
        }

        takePicture.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
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

        update.setOnClickListener {
            updateItem()
        }

        delete.setOnClickListener {
            deleteItem()
        }

        populateItemDetails()
    }

    private fun populateItemDetails() {
        val image = intent.getByteArrayExtra("itemImage")
        val title = intent.getStringExtra("itemTitle")
        val quantity = intent.getIntExtra("itemQuantity", 0)
        val username = intent.getStringExtra("itemUsername")
        val price = intent.getDoubleExtra("itemPrice", 0.0)
        val email = intent.getStringExtra("emailId")
        val size = intent.getStringExtra("itemSize")

        if (image != null) {
            itemImage.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.size))
        }
        itemTitle.setText(title)
        itemQuantity.setText(quantity.toString())
        itemUsername.setText(username)
        itemPrice.setText(price.toString())
        emailId.setText(email)
        itemSize.setText(size)
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
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as? Bitmap
                    imageBitmap?.let {
                        itemImage.setImageBitmap(it)
                    }
                }
                REQUEST_IMAGE_GALLERY -> {
                    val imageUri = data?.data
                    imageUri?.let {
                        val inputStream = contentResolver.openInputStream(it)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        itemImage.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }

    private fun updateItem() {
        // Implement update functionality here
    }

    private fun deleteItem() {
        // Implement delete functionality here
    }

    private fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
