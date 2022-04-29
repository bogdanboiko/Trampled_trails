package com.example.gh_coursework.data.database.response

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.gh_coursework.data.database.entity.*

data class RoutePointsResponse(
    @Embedded
    val coordinate: PointCoordinatesEntity,

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