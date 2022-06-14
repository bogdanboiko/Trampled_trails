package com.example.trampled_trails.data.database.mapper.public

import com.example.trampled_trails.data.database.entity.RouteEntity
import com.example.trampled_trails.domain.entity.PublicRouteDomain

fun mapPublicRouteDomainToEntity(route: PublicRouteDomain): RouteEntity {
    return RouteEntity(route.routeId, route.name, route.description, route.isPublic)
}