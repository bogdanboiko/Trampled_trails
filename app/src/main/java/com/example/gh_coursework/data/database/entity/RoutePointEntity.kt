package com.example.gh_coursework.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "routes_points", primaryKeys = ["routeId", "pointId"])
data class RoutePointEntity(
    @ColumnInfo(name = "routeId")
    val routeId: Int,
    @ColumnInfo(name = "pointId")
    val pointId: Int,
    @ColumnInfo(name = "position")
    val position: Int
)
