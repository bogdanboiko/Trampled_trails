package com.example.gh_coursework.data.database.response

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.gh_coursework.data.database.entity.PointPreviewEntity
import com.example.gh_coursework.data.database.entity.PointImageEntity

data class RoutePointImageResponse(
    @Embedded
    val routePointEntity: PointPreviewEntity,

    @Relation(
        parentColumn = "pointId",
        entityColumn = "pointId",
        associateBy = Junction(PointPreviewEntity::class)
    )
    val pointImages: List<PointImageEntity>,
)