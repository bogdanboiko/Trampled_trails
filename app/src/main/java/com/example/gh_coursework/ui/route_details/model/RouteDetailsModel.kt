package com.example.gh_coursework.ui.route_details.model

import com.example.gh_coursework.ui.model.ImageModel.RouteImageModel

data class RouteDetailsModel(
    val routeId: Long?,
    val name: String?,
    val description: String?,
    val tagList: List<RouteTagModel>,
    val imageList: List<RouteImageModel>,
    val isPublic: Boolean
)