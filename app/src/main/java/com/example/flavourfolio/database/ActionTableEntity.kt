package com.example.flavourfolio.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.flavourfolio.enums.AffixType

@Entity(
    tableName = "actions_table",
    foreignKeys = [ForeignKey(
        entity = Step::class,
        parentColumns = ["step_id"],
        childColumns = ["step_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [
        Index("step_id")
    ]
)
data class Action(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "action_id")
    val aid: Int = 0,

    @ColumnInfo(name = "step_id")
    val sid: Int,

    @ColumnInfo(name = "affix")
    val affix: AffixType,

    @ColumnInfo(name = "detail")
    val detail: String
)