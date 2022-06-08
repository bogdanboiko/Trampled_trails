package com.example.gh_coursework.data.database.mapper.route_preview

import com.example.gh_coursework.data.database.entity.RouteEntity
import com.example.gh_coursework.domain.entity.RouteDomain

fun mapRouteDomainToEntity(routeDomain: RouteDomain): RouteEntity {
    return RouteEntity(
        routeDomain.routeId,
        routeDomain.name,
        routeDomain.description,
        routeDomain.isPublic
    )
}