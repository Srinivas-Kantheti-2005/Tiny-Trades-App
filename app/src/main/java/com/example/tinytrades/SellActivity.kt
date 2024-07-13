package com.example.tinytrades

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SellActivity : AppCompatActivity() {

    private lateinit var backbtn: ImageButton
    private lateinit var image: ImageView
    private lateinit var title: EditText
    private lateinit var size: EditText
    private lateinit var price: EditText
    private lateinit var takepicture: Button

    private val REQUEST_IMAGE_CAPTURE = 1

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell)

        backbtn = findViewById(R.id.backbtn)
        image = findViewById(R.id.image)
        title = findViewById(R.id.title)
        size = findViewById(R.id.size)
        price = findViewById(R.id.price)
        takepicture = findViewById(R.id.takepicture)

        takepicture.setOnClickListener {
            dispatchTakePictureIntent()
        }

        backbtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            image.setImageBitmap(imageBitmap)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}