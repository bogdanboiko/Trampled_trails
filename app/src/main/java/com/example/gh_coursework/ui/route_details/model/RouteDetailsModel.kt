package com.example.gh_coursework.ui.route_details.model

import android.graphics.drawable.Drawable
import com.example.gh_coursework.ui.point_details.model.PointTagModel
import com.example.gh_coursework.ui.private_route.model.PrivateRoutePointModel

data class RouteDetailsModel(
    val routeId: Long,
    val name: String,
    val description: String,
    val rating: Double,
    val tagList: List<PointTagModel>,
    val coordinatesList: List<PrivateRoutePointModel>,
    val imgResources: Drawable?
)