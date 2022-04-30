package com.example.gh_coursework.data.remote.mapper

import com.example.gh_coursework.data.remote.entity.PublicPointResponseEntity
import com.example.gh_coursework.domain.entity.PublicPointDomain

fun mapPublicPointResponseEntityToPublicDomain(point: PublicPointResponseEntity): PublicPointDomain {
    return PublicPointDomain(
        point.pointDocumentId,
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