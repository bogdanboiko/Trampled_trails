package com.example.gh_coursework.data.database.entity

import androidx.room.ColumnInfo

data class DeletedRoutesEntity(
    @ColumnInfo(name = "route_id")
    val routeId: String
)