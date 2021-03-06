package com.example.trampled_trails.ui.private_route.model

import com.example.trampled_trails.ui.point_details.model.PointTagModel
import com.example.trampled_trails.ui.private_image_details.model.ImageModel.PointImageModel
import java.util.*

data class RoutePointModel(
    val pointId: String = UUID.randomUUID().toString(),
    val caption: String = "",
    val description: String = "",
    val tagList: List<PointTagModel> = emptyList(),
    val imageList: List<PointImageModel> = emptyList(),
    val x: Double,
    val y: Double,
    val routeId: String?,
    val isRoutePoint: Boolean,
    val position: Long?
)