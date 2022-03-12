package com.example.gh_coursework.ui.private_route.mapper

import com.example.gh_coursework.ui.private_route.model.PrivateRoutePointModel
import com.mapbox.geojson.Point

fun mapPointToPrivateRoutePointModel(point: Point): PrivateRoutePointModel {
    return PrivateRoutePointModel(null, point.longitude(), point.latitude(), true)
}