package com.example.gh_coursework.data.remote.mapper

import com.example.gh_coursework.data.remote.entity.PublicRoutePointResponseEntity
import com.example.gh_coursework.domain.entity.PublicRoutePointDomain

fun mapPublicRoutePointResponseEntityToDomain(routePoint: PublicRoutePointResponseEntity): PublicRoutePointDomain {
    return PublicRoutePointDomain(
        routePoint.pointDocumentId,
        routePoint.caption,
        routePoint.description,
        routePoint.imageList,
        routePoint.x,
        routePoint.y,
        routePoint.isRoutePoint
    )
}