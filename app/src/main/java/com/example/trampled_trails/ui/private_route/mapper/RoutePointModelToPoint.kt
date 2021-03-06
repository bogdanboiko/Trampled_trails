package com.example.trampled_trails.ui.private_route.mapper

import com.example.trampled_trails.ui.private_route.model.RoutePointModel
import com.mapbox.geojson.Point

fun mapPrivateRoutePointModelToPoint(routePointModel: RoutePointModel): Point {
    return Point.fromLngLat(routePointModel.x, routePointModel.y)
}