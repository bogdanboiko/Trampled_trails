package com.example.gh_coursework.ui.private_route.model

import com.example.gh_coursework.ui.route_details.model.RouteImageModel
import com.example.gh_coursework.ui.route_details.model.RouteTagModel

data class RouteModel(
    val routeId: Long?,
    val name: String?,
    val description: String?,
    val rating: Double?,
    val tagsList: List<RouteTagModel>,
    val imageList: List<RouteImageModel>
)