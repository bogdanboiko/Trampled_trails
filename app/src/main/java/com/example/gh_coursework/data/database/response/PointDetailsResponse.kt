package com.example.gh_coursework.data.database.response

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.gh_coursework.data.database.entity.PointDetailsEntity
import com.example.gh_coursework.data.database.entity.PointImageEntity
import com.example.gh_coursework.data.database.entity.PointTagEntity
import com.example.gh_coursework.data.database.entity.PointsTagsEntity

data class PointDetailsResponse(
    @Embedded
    val pointDetails: PointDetailsEntity,
    @Relation(
        parentColumn = "pointId",
        entityColumn = "tagId",
        associateBy = Junction(PointsTagsEntity::class)
    )
    val tagList: List<PointTagEntity>,
    @Relation(
        parentColumn = "pointId",
        entityColumn = "pointId",
        associateBy = Junction(PointImageEntity::class)
    )
    val imageList: List<PointImageEntity>
)