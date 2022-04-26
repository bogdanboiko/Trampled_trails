package com.example.gh_coursework.ui.private_route.model

import com.example.gh_coursework.ui.point_details.model.PointTagModel
import com.example.gh_coursework.ui.model.ImageModel.PointImageModel


data class RoutePointModel(
    val pointId: String,
    val caption: String,
    val description: String,
    val tagList: List<PointTagModel>,
    val imageList: List<PointImageModel>,
    val x: Double,
    val y: Double,
    val isRoutePoint: Boolean
)