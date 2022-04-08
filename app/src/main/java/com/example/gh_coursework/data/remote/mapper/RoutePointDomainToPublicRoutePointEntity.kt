package com.example.gh_coursework.data.remote.mapper

import com.example.gh_coursework.data.remote.entity.PublicRoutePointEntity
import com.example.gh_coursework.domain.entity.RoutePointDomain

fun mapRoutePointDomainToPublicRoutePointEntity(
    routePoint: RoutePointDomain,
    pointImageUrls: MutableList<String>
): PublicRoutePointEntity {
    return PublicRoutePointEntity(
        routePoint.caption,
        routePoint.description,
        pointImageUrls,
        routePoint.x,
        routePoint.y
    )
}