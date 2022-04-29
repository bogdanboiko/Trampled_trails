package com.example.gh_coursework.data.remote.mapper

import com.example.gh_coursework.data.remote.entity.PublicRoutePointEntity
import com.example.gh_coursework.domain.entity.PointDomain

fun mapRoutePointDomainToPublicRoutePointEntity(
    routePoint: PointDomain,
    imageList: List<String>,
    position: Int
): PublicRoutePointEntity {
    return PublicRoutePointEntity(
        routePoint.caption,
        routePoint.description,
        imageList,
        routePoint.x,
        routePoint.y,
        routePoint.routeId,
        routePoint.isRoutePoint,
        position
    )
}