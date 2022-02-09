package com.example.gh_coursework.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "points")
data class PointCoordinatesEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val pointId: Int,
    @ColumnInfo(name = "x")
    val x: Double,
    @ColumnInfo(name = "y")
    val y: Double
)
