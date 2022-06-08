package com.example.gh_coursework.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "point_tag")
data class PointTagEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tagId")
    val tagId: Long?,
    @ColumnInfo(name = "name")
    val name: String
)
