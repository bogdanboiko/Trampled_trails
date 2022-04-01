package com.example.gh_coursework.data.database.entity

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
    val routeId: Long,
    @ColumnInfo(name = "image")
    val image: String
)