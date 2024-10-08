package com.example.tinytrades.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User(
    @PrimaryKey
    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "password")
    var password: String
)
