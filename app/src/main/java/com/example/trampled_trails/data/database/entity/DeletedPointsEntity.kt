package com.example.trampled_trails.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deleted_points")
data class DeletedPointsEntity(
    @PrimaryKey
    @ColumnInfo(name = "point_id")
    val pointId: String
)