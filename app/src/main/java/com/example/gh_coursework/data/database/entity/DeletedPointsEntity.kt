package com.example.gh_coursework.data.database.entity

import androidx.room.ColumnInfo

data class DeletedPointsEntity(
    @ColumnInfo(name = "point_id")
    val pointId: String
)