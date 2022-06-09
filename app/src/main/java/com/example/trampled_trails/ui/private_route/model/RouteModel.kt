package com.example.trampled_trails.ui.private_route.model

import com.example.trampled_trails.ui.private_image_details.model.ImageModel.RouteImageModel
import com.example.trampled_trails.ui.route_details.model.RouteTagModel

data class RouteModel(
    val routeId: String,
    val name: String? = "",
    val description: String? = "",
    val tagsList: List<RouteTagModel> = emptyList(),
    val imageList: List<RouteImageModel> = emptyList(),
    val isPublic: Boolean
)