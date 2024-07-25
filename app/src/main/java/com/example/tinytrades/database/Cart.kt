package com.example.tinytrades.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "Cart",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("username"),
            childColumns = arrayOf("username"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class Cart (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    val id: Int = 0,

    @ColumnInfo(name = "Title")
    val title: String,

    @ColumnInfo(name = "Image")
    val image: ByteArray?,

    @ColumnInfo(name = "Quantity")
    val quantity: Int,

    @ColumnInfo(name = "Size")
    val size: String,

    @ColumnInfo(name = "Price")
    val price: Double,

    @ColumnInfo(name = "username")
    val sellerUsername: String,

    @ColumnInfo(name = "Buyer_Username")
    val buyerUsername: String
)
