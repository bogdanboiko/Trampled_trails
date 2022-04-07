package com.example.gh_coursework.data.remote.mapper

import com.example.gh_coursework.data.remote.entity.PublicRoutePointEntity
import com.example.gh_coursework.domain.entity.RoutePointDomain

fun mapRoutePointDomainToPublicRoutePointEntity(routePoint: RoutePointDomain): PublicRoutePointEntity {
    return PublicRoutePointEntity(
        routePoint.caption,
        routePoint.description,
        routePoint.tagList.map { it.name },
        routePoint.imageList.map { it.image },
        routePoint.x,
        routePoint.y
    )
}