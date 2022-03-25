package com.example.gh_coursework.ui.private_route.mapper

import com.example.gh_coursework.ui.point_details.model.PointImageModel
import com.example.gh_coursework.ui.route_details.model.RouteImageModel

fun mapRouteImageModelToPointImageModel(image: RouteImageModel): PointImageModel {
    return PointImageModel(image.routeId, image.image)
}