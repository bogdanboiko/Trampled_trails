package com.example.gh_coursework.ui.route_details.model

data class RouteDetailsModel(
    val routeId: Long?,
    val name: String?,
    val description: String?,
    val rating: Double?,
    val tagList: List<RouteTagModel>,
    val imageList: List<RouteImageModel>
)