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
    )]
)
data class RoutePointEntity(
    @ColumnInfo(name = "routeId")
    var routeId: Long?,
    @PrimaryKey
    @ColumnInfo(name = "pointId")
    val pointId: Long,
    @ColumnInfo(name = "position")
    val position: Int
)
