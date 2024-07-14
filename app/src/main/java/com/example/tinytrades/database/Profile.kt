package com.example.tinytrades.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Profile",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["username"],
        childColumns = ["username"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Profile(
    @PrimaryKey
    @ColumnInfo(name = "emailid")
    val emailId: String,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "image")
    val image: Int,

    @ColumnInfo(name = "First Name")
    val firstname: String,

    @ColumnInfo(name = "Last Name")
    val lastname: String,

    @ColumnInfo(name = "Gender")
    val gender: String,

    @ColumnInfo(name = "Mobile")
    val mobile: Long,

    @ColumnInfo(name = "DNo")
    val dno: String,

    @ColumnInfo(name = "Street")
    val street: String,

    @ColumnInfo(name = "Village")
    val village: String,

    @ColumnInfo(name = "Pin code")
    val pinCode: Long,

    @ColumnInfo(name = "Mandal")
    val mandal: String,

    @ColumnInfo(name = "District")
    val district: String
)
