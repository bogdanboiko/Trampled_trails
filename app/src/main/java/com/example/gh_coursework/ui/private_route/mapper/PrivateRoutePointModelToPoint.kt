package com.example.gh_coursework.ui.private_route.mapper

import com.example.gh_coursework.ui.private_route.model.PrivateRoutePointModel
import com.mapbox.geojson.Point

fun mapPrivateRoutePointModelToPoint(privateRoutePointModel: PrivateRoutePointModel): Point {
    return Point.fromLngLat(privateRoutePointModel.x, privateRoutePointModel.y)
}