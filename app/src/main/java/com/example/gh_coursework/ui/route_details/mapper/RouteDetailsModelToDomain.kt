package com.example.gh_coursework.ui.route_details.mapper

import com.example.gh_coursework.domain.entity.RouteDetailsDomain
import com.example.gh_coursework.ui.route_details.model.RouteDetailsModel

fun mapRouteDetailsModelToDomain(route: RouteDetailsModel): RouteDetailsDomain {
    return RouteDetailsDomain(
        route.routeId,
        route.name,
        route.description,
        route.rating,
        route.tagList.map(::mapRouteTagModelToDomain)
    )
}