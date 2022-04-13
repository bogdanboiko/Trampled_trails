package com.example.gh_coursework.ui.public_route.mapper

import com.example.gh_coursework.ui.public_route.model.RoutePointModel
import com.mapbox.geojson.Point

fun mapPrivateRoutePointModelToPoint(routePointModel: RoutePointModel): Point {
    return Point.fromLngLat(routePointModel.x, routePointModel.y)
}