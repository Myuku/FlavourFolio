package com.example.flavourfolio.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "details",
    foreignKeys = [ForeignKey(
        entity = Step::class,
        parentColumns = ["sid"],
        childColumns = ["sid"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Detail(
    @PrimaryKey(autoGenerate = true)
    val aid: Int = 0,
    val sid: Int,
    val affix: String, // Use String or Enum class here
    val detail: String
)