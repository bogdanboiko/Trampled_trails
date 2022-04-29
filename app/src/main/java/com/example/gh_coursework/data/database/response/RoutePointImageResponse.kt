package com.example.gh_coursework.data.database.response

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.gh_coursework.data.database.entity.PointCoordinatesEntity
import com.example.gh_coursework.data.database.entity.PointImageEntity

data class RoutePointImageResponse(
    @Embedded
    val routePointEntity: PointCoordinatesEntity,

    @Relation(
        parentColumn = "pointId",
        entityColumn = "pointId",
        associateBy = Junction(PointCoordinatesEntity::class)
    )
    val pointImages: List<PointImageEntity>,
)