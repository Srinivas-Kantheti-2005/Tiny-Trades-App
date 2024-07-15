package com.example.tinytrades.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Item",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("username"),
            childColumns = arrayOf("username"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Profile::class,
            parentColumns = arrayOf("emailid"),
            childColumns = arrayOf("emailid"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Item(
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
    val username: String,

    @ColumnInfo(name = "emailid")
    val emailid: String
)
