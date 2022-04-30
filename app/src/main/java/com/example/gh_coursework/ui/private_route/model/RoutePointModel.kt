package com.example.gh_coursework.ui.private_route.model

import com.example.gh_coursework.ui.point_details.model.PointTagModel
import com.example.gh_coursework.ui.private_image_details.model.ImageModel.PointImageModel


data class RoutePointModel(
    val pointId: String,
    val caption: String,
    val description: String,
    val tagList: List<PointTagModel>,
    val imageList: List<PointImageModel>,
    val x: Double,
    val y: Double,
    val routeId: String?,
    val isRoutePoint: Boolean,
    val position: Long?
)