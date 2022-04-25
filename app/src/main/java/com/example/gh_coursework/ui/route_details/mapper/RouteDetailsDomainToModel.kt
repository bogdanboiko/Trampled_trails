package com.example.gh_coursework.ui.route_details.mapper

import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.ui.route_details.model.RouteDetailsModel

fun mapRouteDetailsDomainToModel(route: RouteDomain): RouteDetailsModel {
    return RouteDetailsModel(
        route.routeId,
        route.name,
        route.description,
        route.tagsList.map(::mapRouteTagDomainToModel),
        route.imageList.map(::mapRouteImageDomainToModel),
        route.isPublic
    )
}