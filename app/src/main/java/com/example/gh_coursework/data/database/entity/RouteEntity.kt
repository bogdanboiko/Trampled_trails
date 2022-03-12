package com.example.gh_coursework.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "route_details")
data class RouteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "routeId")
    var routeId: Int?,
    @ColumnInfo(name = "routeName")
    val name: String?,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "rating")
    val rating: Double?
)