package com.example.gh_coursework.ui.route_details.mapper

import com.example.gh_coursework.domain.entity.RouteDetailsDomain
import com.example.gh_coursework.ui.route_details.model.RouteDetailsModel

fun mapRouteDetailsDomainToModel(route: RouteDetailsDomain): RouteDetailsModel {
    return RouteDetailsModel(
        route.routeId,
        route.name,
        route.description,
        route.rating,
        route.tagsList.map(::mapRouteTagDomainToModel),
        route.imageList.map(::mapRouteImageDomainToModel),
        null
    )
}