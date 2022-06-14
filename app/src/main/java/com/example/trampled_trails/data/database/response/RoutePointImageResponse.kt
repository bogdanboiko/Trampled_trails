package com.example.trampled_trails.data.database.response

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.trampled_trails.data.database.entity.PointPreviewEntity
import com.example.trampled_trails.data.database.entity.PointImageEntity

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