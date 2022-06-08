package com.example.gh_coursework.ui.route_details.mapper

import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.ui.route_details.model.RouteDetailsModel

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