package com.example.trampled_trails.ui.route_details.mapper

import com.example.trampled_trails.domain.entity.RouteDomain
import com.example.trampled_trails.ui.route_details.model.RouteDetailsModel

fun mapRouteDetailsModelToDomain(route: RouteDetailsModel): RouteDomain {
    return RouteDomain(
        route.routeId,
        route.name,
        route.description,
        route.tagList.map(::mapRouteTagModelToDomain),
        route.imageList.map(::mapRouteImageModelToDomain),
        route.isPublic
    )
}