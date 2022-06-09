package com.example.trampled_trails.ui.route_details.model

import com.example.trampled_trails.ui.private_image_details.model.ImageModel.RouteImageModel

data class RouteDetailsModel(
    val routeId: String,
    val name: String?,
    val description: String?,
    val tagList: List<RouteTagModel>,
    val imageList: List<RouteImageModel>,
    val isPublic: Boolean
)