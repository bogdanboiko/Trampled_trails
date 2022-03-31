package com.example.gh_coursework.data.database.response

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.gh_coursework.data.database.entity.PointImageEntity
import com.example.gh_coursework.data.database.entity.RoutePointEntity

data class RoutePointImageResponse(
    @Embedded
    val routePointEntity: RoutePointEntity,

    @Relation(
        parentColumn = "pointId",
        entityColumn = "pointId",
        associateBy = Junction(RoutePointEntity::class)
    )
    val pointImages: List<PointImageEntity>,
)