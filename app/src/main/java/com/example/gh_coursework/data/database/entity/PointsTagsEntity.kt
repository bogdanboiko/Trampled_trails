package com.example.gh_coursework.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "points_tags", primaryKeys = ["pointId", "tagId"])
data class PointsTagsEntity(
    @ColumnInfo(name = "pointId")
    val pointId: Int,
    @ColumnInfo(name = "tagId")
    val tagId: Int
)