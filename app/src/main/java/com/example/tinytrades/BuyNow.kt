package com.example.tinytrades

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tinytrades.database.AppDatabase
import com.example.tinytrades.database.CartDao
import com.example.tinytrades.database.ItemDao
import com.example.tinytrades.database.Order
import com.example.tinytrades.database.OrderDao
import com.example.tinytrades.database.ProfileDao
import com.example.tinytrades.database.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class BuyNow : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var profileDao: ProfileDao
    private lateinit var itemDao: ItemDao
    private lateinit var cartDao: CartDao
    private lateinit var orderDao: OrderDao

    private lateinit var backbtn: ImageButton
    private lateinit var profileImage: ImageView
    private lateinit var buyerUsername: TextView
    private lateinit var buyerName: TextView
    private lateinit var gender: TextView
    private lateinit var mobileNo: TextView
    private lateinit var emailId: TextView

    private lateinit var dNo: TextView
    private lateinit var street: TextView
    private lateinit var village: TextView
    private lateinit var pinCode: TextView
    private lateinit var mandal: TextView
    private lateinit var district: TextView

    private lateinit var cartImage: ImageView
    private lateinit var title: TextView
    private lateinit var size: TextView
    private lateinit var quantity: EditText
    private lateinit var price: TextView

    private lateinit var confirmBuy: Button
    private lateinit var update: Button

    private lateinit var usernameExtra: String
    private var itemId: Int = -1
    private var cartId: Int = -1

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_now)

        // Initialize database and DAOs
        database = AppDatabase.getDatabase(applicationContext)
        userDao = database.userDao()
        profileDao = database.profileDao()
        itemDao = database.itemDao()
        cartDao = database.cartDao()
        orderDao = database.orderDao()

        // Initialize views
        backbtn = findViewById(R.id.backbtn)
        profileImage = findViewById(R.id.profileimage)
        buyerUsername = findViewById(R.id.username)
        buyerName = findViewById(R.id.buyerName)
        gender = findViewById(R.id.gender)
        mobileNo = findViewById(R.id.mobileno)
        emailId = findViewById(R.id.emailid)

        dNo = findViewById(R.id.dno)
        street = findViewById(R.id.street)
        village = findViewById(R.id.village)
        pinCode = findViewById(R.id.pincode)
        mandal = findViewById(R.id.mandal)
        district = findViewById(R.id.district)

        cartImage = findViewById(R.id.cart_image)
        title = findViewById(R.id.cart_title)
        size = findViewById(R.id.cart_size)
        quantity = findViewById(R.id.cart_quantity)
        price = findViewById(R.id.cart_price)

        confirmBuy = findViewById(R.id.confirm_buy)
        update = findViewById(R.id.update)

        // Get extras from intent
        usernameExtra = intent.getStringExtra("USERNAME") ?: ""
        itemId = intent.getIntExtra("ITEM_ID", -1)
        cartId = intent.getIntExtra("CART_ID", -1)

        loadProfile()
        loadProfileAddress()
        loadItem()


        // Set up listeners
        backbtn.setOnClickListener {
            onBackPressed()
        }

        confirmBuy.setOnClickListener {
            confirmOrder()
        }

        update.setOnClickListener {
            updateAddress()
        }
    }

    private fun loadProfile() {
        lifecycleScope.launch {
            try {
                val profile = profileDao.getProfileByUsername(usernameExtra)
                if (profile != null) {
                    buyerUsername.text = profile.username
                    buyerName.text = "${profile.firstname} ${profile.lastname}"
                    gender.text = profile.gender
                    mobileNo.text = profile.mobile.toString()
                    emailId.text = profile.emailId

                    profileImage.setImageResource(
                        if (profile.gender == "Male") R.drawable.men else R.drawable.women
                    )
                } else {
                    showToast("Profile not found")
                }
            } catch (e: Exception) {
                showToast("Error loading profile: ${e.message}")
            }
        }
    }

    private  fun loadProfileAddress() {
        lifecycleScope.launch {
            try {
                val profileAddress = profileDao.getProfileByUsername(usernameExtra)
                if(profileAddress != null) {
                    dNo.text = profileAddress.dno
                    street.text = profileAddress.street
                    village.text = profileAddress.village
                    pinCode.text = profileAddress.pinCode.toString()
                    mandal.text = profileAddress.mandal
                    district.text = profileAddress.district
                }
                else {
                    showToast("profile address not found")
                }
            }
            catch (e: Exception) {
                showToast("Error at loading profile: ${e.message}")
            }
        }
    }

    private fun loadItem() {
        val imageByteArray = intent.getByteArrayExtra("ITEM_IMAGE")
        val itemTitle = intent.getStringExtra("ITEM_TITLE") ?: ""
        val itemSize = intent.getStringExtra("ITEM_SIZE") ?: ""
        val itemQuantity = intent.getStringExtra("ITEM_QUANTITY") ?: ""
        val itemPrice = intent.getStringExtra("ITEM_PRICE") ?: ""

        imageByteArray?.let {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            cartImage.setImageBitmap(bitmap)
        } ?: cartImage.setImageResource(android.R.color.transparent)

        title.text = itemTitle
        size.text = itemSize
        quantity.setText(itemQuantity)
        price.text = itemPrice
    }

    private fun confirmOrder() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val existingOrderByUsername = orderDao.getOrderByUsername(usernameExtra)
                    val existingOrderByTitle = orderDao.getOrderByTitle(title.text.toString())
                    if (existingOrderByUsername.isNotEmpty() && existingOrderByTitle.isNotEmpty()) {
                        withContext(Dispatchers.Main) {
                            showToast("Order already exists")
                        }
                        return@withContext
                    }

                    val newOrder = Order(
                        buyerUsername = usernameExtra,
                        Name = buyerName.text.toString(),
                        gender = gender.text.toString(),
                        mobileNo = mobileNo.text.toString().toLong(),
                        emailId = emailId.text.toString(),
                        dNo = dNo.text.toString(),
                        street = street.text.toString(),
                        village = village.text.toString(),
                        pinCode = pinCode.text.toString().toLong(),
                        mandal = mandal.text.toString(),
                        district = district.text.toString(),
                        image = cartImage.drawable.toByteArray(),
                        title = title.text.toString(),
                        size = size.text.toString(),
                        quantity = quantity.text.toString().toInt(),
                        price = price.text.toString().toDouble()
                    )
                    orderDao.insert(newOrder)
                    cartDao.deleteItem(itemId, usernameExtra)
                    withContext(Dispatchers.Main) {
                        showToast("Order placed successfully. Order must be cash on delivery")
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        showToast("Error placing order: ${e.message}")
                    }
                }
            }
        }
    }

    private fun updateAddress() {
        val dno = dNo.text.toString()
        val street = street.text.toString()
        val village = village.text.toString()
        val pincode = pinCode.text.toString()
        val mandal = mandal.text.toString()
        val district = district.text.toString()

        if (isValidProfileAddress(dno, street, village, pincode, mandal, district)) {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val profileAddress = profileDao.getProfileByUsername(usernameExtra)
                    profileAddress?.let {
                        it.dno = dno
                        it.street = street
                        it.village = village
                        it.pinCode = pincode.toLong()
                        it.mandal = mandal
                        it.district = district
                        profileDao.update(it)
                    }
                    return@withContext profileAddress
                }?.let {
                    withContext(Dispatchers.Main) {
                        showToast("Address updated successfully")
                    }
                } ?: withContext(Dispatchers.Main) {
                    showToast("User not found")
                }
            }
        }
    }

    private fun isValidProfileAddress(dno: String, street: String, village: String, pincode: String, mandal: String, district: String): Boolean {
        return when {
            dno.isEmpty() -> {
                showToast("Fill the DNO field")
                false
            }
            street.isEmpty() -> {
                showToast("Fill the street field")
                false
            }
            village.isEmpty() -> {
                showToast("Fill the village field")
                false
            }
            pincode.isEmpty() -> {
                showToast("Fill the pincode field")
                false
            }
            mandal.isEmpty() -> {
                showToast("Fill the mandal field")
                false
            }
            district.isEmpty() -> {
                showToast("Fill the district field")
                false
            }
            else -> true
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun Drawable.toByteArray(): ByteArray {
        val bitmap = (this as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}
