package com.example.trampled_trails.data.database.response

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.trampled_trails.data.database.entity.*

data class PointResponse(
    @Embedded
    val coordinate: PointPreviewEntity,

    @Relation(
        parentColumn = "pointId",
        entityColumn = "pointId",
    )
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
    )
    val imageList: List<PointImageEntity>
)