package com.example.trampled_trails.data.database.mapper.route_preview

import com.example.trampled_trails.data.database.entity.RouteEntity
import com.example.trampled_trails.domain.entity.RouteDomain

fun mapRouteDomainToEntity(routeDomain: RouteDomain): RouteEntity {
    return RouteEntity(
        routeDomain.routeId,
        routeDomain.name,
        routeDomain.description,
        routeDomain.isPublic
    )
}