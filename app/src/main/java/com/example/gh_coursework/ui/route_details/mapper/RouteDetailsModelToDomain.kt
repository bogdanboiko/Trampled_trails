package com.example.gh_coursework.ui.route_details.mapper

import com.example.gh_coursework.domain.entity.PointPreviewDomain
import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.ui.private_route.mapper.mapPointModelToDomain
import com.example.gh_coursework.ui.route_details.model.RouteDetailsModel

fun mapRouteDetailsModelToDomain(route: RouteDetailsModel): RouteDomain {
    val pointCoordinatesDomainList = mutableListOf<PointPreviewDomain>()

    route.coordinatesList.forEach {
        pointCoordinatesDomainList.add(mapPointModelToDomain(it))
    }

    return RouteDomain(
        route.routeId,
        route.name,
        route.description,
        route.rating,
        pointCoordinatesDomainList
    )
}