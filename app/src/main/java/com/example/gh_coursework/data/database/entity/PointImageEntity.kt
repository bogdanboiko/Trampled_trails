package com.example.gh_coursework.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "point_image", primaryKeys = ["pointId", "image"], foreignKeys = [ForeignKey(
        entity = PointCoordinatesEntity::class,
        onDelete = ForeignKey.CASCADE,
        parentColumns = arrayOf("pointId"),
        childColumns = arrayOf("pointId")
    )]
)
data class PointImageEntity(
    @ColumnInfo(name = "pointId")
    val pointId: String,
    @ColumnInfo(name = "image")
    val image: String
)