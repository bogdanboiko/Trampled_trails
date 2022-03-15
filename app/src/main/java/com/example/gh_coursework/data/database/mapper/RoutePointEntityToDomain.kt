package com.example.gh_coursework.data.database.mapper

import com.example.gh_coursework.data.database.entity.RoutePointEntity
import com.example.gh_coursework.domain.entity.RoutePointDomain

fun mapRoutePointEntityToDomain(routePointEntity: RoutePointEntity): RoutePointDomain {
    return RoutePointDomain(
        routePointEntity.routeId,
        routePointEntity.pointId,
        routePointEntity.position
    )
}