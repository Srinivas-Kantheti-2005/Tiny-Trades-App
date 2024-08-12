package com.example.tinytrades.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Order",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("username"),
            childColumns = arrayOf("buyerUsername"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Profile::class,
            parentColumns = arrayOf("emailid"),
            childColumns = arrayOf("emailId"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Item::class,
            parentColumns = arrayOf("Id"),
            childColumns = arrayOf("itemId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class Order(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "orderId")
    val orderId: Int = 0,

    @ColumnInfo(name = "buyerUsername")
    val buyerUsername: String,

    @ColumnInfo(name = "Name")
    val Name: String,

    @ColumnInfo(name = "gender")
    val gender: String,

    @ColumnInfo(name = "mobileNo")
    val mobileNo: Long,

    @ColumnInfo(name = "emailId")
    val emailId: String,

    @ColumnInfo(name = "dNo")
    val dNo: String,

    @ColumnInfo(name = "street")
    val street: String,

    @ColumnInfo(name = "village")
    val village: String,

    @ColumnInfo(name = "pinCode")
    val pinCode: Long,

    @ColumnInfo(name = "mandal")
    val mandal: String,

    @ColumnInfo(name = "district")
    val district: String,

    @ColumnInfo(name = "itemId")
    val itemId: Int,

    @ColumnInfo(name = "image")
    val image: ByteArray?,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "size")
    val size: String,

    @ColumnInfo(name = "quantity")
    val quantity: Int,

    @ColumnInfo(name = "price")
    val price: Double
)
