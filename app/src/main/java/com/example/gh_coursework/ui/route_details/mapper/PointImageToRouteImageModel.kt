package com.example.gh_coursework.ui.route_details.mapper

import com.example.gh_coursework.ui.point_details.model.PointImageModel
import com.example.gh_coursework.ui.route_details.model.RouteImageModel

fun mapPointImageToRouteImageModel(image: PointImageModel): RouteImageModel {
    return RouteImageModel(image.pointId, image.image)
}