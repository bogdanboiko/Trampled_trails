package com.example.gh_coursework.data.database.response

import androidx.room.Embedded
import com.example.gh_coursework.data.database.entity.PointCoordinatesEntity
import com.example.gh_coursework.data.database.entity.RouteEntity

data class RoutePreviewResponse(
    @Embedded
    val route: RouteEntity,
    @Embedded
    val coordinate: PointCoordinatesEntity?
)