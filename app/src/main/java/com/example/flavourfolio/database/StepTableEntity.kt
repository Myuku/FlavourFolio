package com.example.flavourfolio.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "steps",
    foreignKeys = [ForeignKey(
        entity = Recipe::class,
        parentColumns = ["rid"],
        childColumns = ["rid"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Step(
    @PrimaryKey(autoGenerate = true)
    val sid: Int = 0,
    val rid: Int,
    val step: Int,
    val action: String,
    val food: String
)