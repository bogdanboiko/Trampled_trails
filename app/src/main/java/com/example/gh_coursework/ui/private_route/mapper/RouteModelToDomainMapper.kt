package com.example.gh_coursework.ui.private_route.mapper

import com.example.gh_coursework.domain.entity.PointPreviewDomain
import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.ui.private_point.mapper.mapPointModelToDomain
import com.example.gh_coursework.ui.private_route.model.PrivateRouteModel
import com.mapbox.geojson.Point

fun mapRouteModelToDomainMapper(route: PrivateRouteModel): RouteDomain {
    val pointCoordinatesDomainList = mutableListOf<PointPreviewDomain>()

    route.coordinatesList.forEach {
        pointCoordinatesDomainList.add(mapPointModelToDomain(it))
    }

    return RouteDomain(route.routeId, route.name, route.description, 0.0, pointCoordinatesDomainList)
}