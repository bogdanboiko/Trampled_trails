package com.example.trampled_trails.data.remote.mapper

import com.example.trampled_trails.data.remote.entity.PublicPointEntity
import com.example.trampled_trails.domain.entity.PointDomain

fun mapPointDomainToPublicPointEntity(
    point: PointDomain,
    imageList: List<String>,
    uid: String,
    position: Int
): PublicPointEntity {
    return PublicPointEntity(
        point.caption,
        point.description,
        point.tagsList.map { it.name },
        imageList,
        point.x,
        point.y,
        point.routeId,
        uid,
        point.isRoutePoint,
        position
    )
}