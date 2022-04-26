package com.example.gh_coursework.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "point_coordinates")
data class PointCoordinatesEntity(
    @PrimaryKey
    @ColumnInfo(name = "pointId")
    val pointId: String,
    @ColumnInfo(name = "x")
    val x: Double,
    @ColumnInfo(name = "y")
    val y: Double,
    @ColumnInfo(name = "isRoutePoint")
    val isRoutePoint: Boolean
)
