package com.example.gh_coursework.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "points_tags", primaryKeys = ["pointId", "tagId"],
    foreignKeys = [ForeignKey(
        entity = PointTagEntity::class,
        onDelete = ForeignKey.CASCADE,
        parentColumns = arrayOf("tagId"),
        childColumns = arrayOf("tagId")
    ),
        ForeignKey(
            entity = PointCoordinatesEntity::class,
            onDelete = ForeignKey.CASCADE,
            parentColumns = arrayOf("pointId"),
            childColumns = arrayOf("pointId")
        )]
)
data class PointsTagsEntity(
    @ColumnInfo(name = "pointId")
    val pointId: Long,
    @ColumnInfo(name = "tagId")
    val tagId: Long
)