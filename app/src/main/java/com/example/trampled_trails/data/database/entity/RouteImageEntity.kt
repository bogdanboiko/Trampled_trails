package com.example.trampled_trails.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "route_image", primaryKeys = ["routeId", "image"], foreignKeys = [ForeignKey(
        entity = RouteEntity::class,
        onDelete = ForeignKey.CASCADE,
        parentColumns = arrayOf("routeId"),
        childColumns = arrayOf("routeId")
    )]
)
data class RouteImageEntity(
    @ColumnInfo(name = "routeId")
    val routeId: String,
    @ColumnInfo(name = "image")
    val image: String,
    @ColumnInfo(name = "isUploaded")
    val isUploaded: Boolean
)