package com.example.gh_coursework.data.database.response

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.gh_coursework.data.database.entity.PointCoordinatesEntity
import com.example.gh_coursework.data.database.entity.RouteEntity
import com.example.gh_coursework.data.database.entity.RoutePointEntity

data class RoutePreviewResponse(
    @Embedded
    val route: RouteEntity,
    @Relation(
        parentColumn = "routeId",
        entityColumn = "pointId",
        associateBy = Junction(RoutePointEntity::class)
    )
    val coordinates: List<PointCoordinatesEntity>
)