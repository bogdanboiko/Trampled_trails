package com.example.trampled_trails.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "route_details")
data class RouteEntity(
    @PrimaryKey
    @ColumnInfo(name = "routeId")
    var routeId: String,
    @ColumnInfo(name = "routeName")
    val name: String?,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "isPublic")
    val isPublic: Boolean
)