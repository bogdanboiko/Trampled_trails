package com.example.trampled_trails.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "point_image", primaryKeys = ["pointId", "image"], foreignKeys = [ForeignKey(
        entity = PointPreviewEntity::class,
        onDelete = ForeignKey.CASCADE,
        parentColumns = arrayOf("pointId"),
        childColumns = arrayOf("pointId")
    )]
)
data class PointImageEntity(
    @ColumnInfo(name = "pointId")
    val pointId: String,
    @ColumnInfo(name = "image")
    val image: String,
    @ColumnInfo(name = "isUploaded")
    val isUploaded: Boolean
)