package com.example.flavourfolio.database

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flavourfolio.enums.RecipeType

@Entity(tableName = "recipes_table")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "recipe_id")
    val rid: Int = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "image")
    val image: Bitmap? = null,

    @ColumnInfo(name = "type")
    val type: RecipeType
)