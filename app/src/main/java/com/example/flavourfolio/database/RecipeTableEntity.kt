package com.example.flavourfolio.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val rid: Int = 0,
    val name: String,
    val image: ByteArray?,
    val type: String
)