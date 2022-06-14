package com.example.trampled_trails.data.remote.mapper

import com.example.trampled_trails.data.remote.entity.PublicPointResponseEntity
import com.example.trampled_trails.domain.entity.PublicPointDomain

fun mapPublicPointResponseEntityToPublicDomain(point: PublicPointResponseEntity): PublicPointDomain {
    return PublicPointDomain(
        point.pointId,
        point.caption,
        point.description,
        point.tagsList,
        point.imageList,
        point.x,
        point.y,
        point.routeId,
        point.isRoutePoint,
        point.position
    )
}