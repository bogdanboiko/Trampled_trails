package com.example.trampled_trails.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "point_details", foreignKeys = [ForeignKey(entity = PointPreviewEntity::class,
        onDelete = CASCADE,
        parentColumns = arrayOf("pointId"),
        childColumns = arrayOf("pointId"))])
data class PointDetailsEntity(
    @PrimaryKey
    @ColumnInfo(name = "pointId")
    val pointId: String,
    @ColumnInfo(name = "caption")
    val caption: String,
    @ColumnInfo(name = "description")
    val description: String
)
