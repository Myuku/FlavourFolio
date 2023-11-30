package com.example.flavourfolio.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "steps_table",
    foreignKeys = [ForeignKey(
        entity = Recipe::class,
        parentColumns = ["recipe_id"],
        childColumns = ["recipe_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [
        Index("recipe_id")
    ]
)
data class Step(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "step_id")
    val sid: Int = 0,

    @ColumnInfo(name = "recipe_id")
    val rid: Int,

    @ColumnInfo(name = "step")
    var step: Int,

    @ColumnInfo(name = "action")
    var action: String,

    @ColumnInfo(name = "food")
    var food: String
)