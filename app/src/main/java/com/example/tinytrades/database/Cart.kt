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
            childColumns = arrayOf("sellerUsername"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Cart (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    val id: Int = 0,

    @ColumnInfo(name = "Title")
    var title: String,

    @ColumnInfo(name = "Image")
    val image: ByteArray?,

    @ColumnInfo(name = "Quantity")
    var quantity: Int,

    @ColumnInfo(name = "Size")
    var size: String,

    @ColumnInfo(name = "Price")
    var price: Double,

    @ColumnInfo(name = "sellerUsername")
    val sellerUsername: String,

    @ColumnInfo(name = "buyerUsername")
    val buyerUsername: String
)