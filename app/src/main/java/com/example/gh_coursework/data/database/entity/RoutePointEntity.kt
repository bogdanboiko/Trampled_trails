package com.example.gh_coursework.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routes_points")
data class RoutePointEntity(
    @ColumnInfo(name = "parentRouteId")
    var routeId: Long?,
    @PrimaryKey
    @ColumnInfo(name = "pointId")
    val pointId: Long,
    @ColumnInfo(name = "position")
    val position: Int
)
