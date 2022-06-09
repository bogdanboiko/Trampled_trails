package com.example.trampled_trails.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deleted_images")
data class DeletedImageEntity(
    @PrimaryKey
    @ColumnInfo(name = "imageUrl")
    val imageUrl: String
    )