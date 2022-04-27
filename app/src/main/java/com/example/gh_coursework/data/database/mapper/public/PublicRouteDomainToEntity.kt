package com.example.gh_coursework.data.database.mapper.public

import com.example.gh_coursework.data.database.entity.RouteEntity
import com.example.gh_coursework.domain.entity.PublicRouteDomain

fun mapPublicRouteDomainToEntity(route: PublicRouteDomain): RouteEntity {
    return RouteEntity(route.routeId, route.name, route.description, route.isPublic)
}