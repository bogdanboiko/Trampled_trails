package com.example.gh_coursework.data.remote.mapper

import com.example.gh_coursework.data.remote.entity.PublicPointEntity
import com.example.gh_coursework.domain.entity.PointDomain

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