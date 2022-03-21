package com.example.gh_coursework.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "route_tags", primaryKeys = ["routeId", "tagId"],
    foreignKeys = [
        ForeignKey(
        entity = RouteTagEntity::class,
        onDelete = ForeignKey.CASCADE,
        parentColumns = arrayOf("tagId"),
        childColumns = arrayOf("tagId")
        )
    ]
)
data class RouteTagsEntity(
    @ColumnInfo(name = "routeId")
    val routeId: Long,
    @ColumnInfo(name = "tagId")
    val tagId: Long
)