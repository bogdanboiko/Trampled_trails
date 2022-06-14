package com.example.trampled_trails.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "point_coordinates", foreignKeys = [ForeignKey(
    entity = RouteEntity::class,
    onDelete = ForeignKey.CASCADE,
    parentColumns = arrayOf("routeId"),
    childColumns = arrayOf("routeId")
)])
data class PointPreviewEntity(
    @PrimaryKey
    @ColumnInfo(name = "pointId")
    val pointId: String,
    @ColumnInfo(name = "x")
    val x: Double,
    @ColumnInfo(name = "y")
    val y: Double,
    @ColumnInfo(name = "routeId")
    val routeId: String?,
    @ColumnInfo(name = "isRoutePoint")
    val isRoutePoint: Boolean,
    @ColumnInfo(name = "position")
    val position: Long?
)
