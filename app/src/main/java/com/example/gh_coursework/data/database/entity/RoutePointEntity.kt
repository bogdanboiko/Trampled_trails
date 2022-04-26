package com.example.gh_coursework.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "routes_points", foreignKeys = [ForeignKey(
        entity = PointCoordinatesEntity::class,
        onDelete = CASCADE,
        parentColumns = arrayOf("pointId"),
        childColumns = arrayOf("pointId")
    ), ForeignKey(
        entity = RouteEntity::class,
        onDelete = CASCADE,
        parentColumns = arrayOf("routeId"),
        childColumns = arrayOf("routeId")
    )]
)
data class RoutePointEntity(
    @ColumnInfo(name = "routeId")
    var routeId: String,
    @PrimaryKey
    @ColumnInfo(name = "pointId")
    val pointId: String,
    @ColumnInfo(name = "position")
    val position: Int
)
