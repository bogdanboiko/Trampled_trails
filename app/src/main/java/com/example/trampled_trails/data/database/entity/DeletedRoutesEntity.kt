package com.example.trampled_trails.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deleted_routes")
data class DeletedRoutesEntity(
    @PrimaryKey
    @ColumnInfo(name = "route_id")
    val routeId: String
)