package com.example.gh_coursework.ui.private_route.model

import com.example.gh_coursework.ui.point_details.model.PointImageModel
import com.example.gh_coursework.ui.point_details.model.PointTagModel

data class RoutePointModel(
    val pointId: Long?,
    val caption: String,
    val description: String,
    val tagList: List<PointTagModel>,
    val imageList: List<PointImageModel>,
    val x: Double,
    val y: Double,
    val isRoutePoint: Boolean
)