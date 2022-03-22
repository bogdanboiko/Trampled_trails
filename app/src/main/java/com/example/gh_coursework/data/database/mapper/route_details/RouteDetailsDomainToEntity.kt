package com.example.gh_coursework.data.database.mapper.route_details

import com.example.gh_coursework.data.database.entity.RouteEntity
import com.example.gh_coursework.domain.entity.RouteDetailsDomain

fun mapRouteDetailsDomainToEntity(route: RouteDetailsDomain): RouteEntity {
    return RouteEntity(
        route.routeId,
        route.name,
        route.description,
        route.rating
    )
}