package com.example.gh_coursework.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "point_details", foreignKeys = [ForeignKey(entity = PointCoordinatesEntity::class,
        onDelete = CASCADE,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("pointId"))])
data class PointDetailsEntity(
    @PrimaryKey
    @ColumnInfo(name = "pointId")
    val pointId: Int,
    @ColumnInfo(name = "tag")
    val tag: String,
    @ColumnInfo(name = "caption")
    val caption: String,
    @ColumnInfo(name = "description")
    val description: String
)
