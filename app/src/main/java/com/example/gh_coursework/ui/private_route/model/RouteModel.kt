package com.example.gh_coursework.ui.private_route.model

import com.example.gh_coursework.ui.private_image_details.model.ImageModel.RouteImageModel
import com.example.gh_coursework.ui.route_details.model.RouteTagModel

data class RouteModel(
    val routeId: String,
    val name: String?,
    val description: String?,
    val tagsList: List<RouteTagModel>,
    val imageList: List<RouteImageModel>,
    val isPublic: Boolean
)