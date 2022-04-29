package com.example.gh_coursework.ui.public_route.mapper

import com.example.gh_coursework.domain.entity.PublicRoutePointDomain
import com.example.gh_coursework.ui.public_route.model.RoutePointModel

fun mapPublicRoutePointModelToDomain(point: RoutePointModel): PublicRoutePointDomain {
    with(point) {
        return PublicRoutePointDomain(
            pointId,
            caption,
            description,
            imageList,
            x,
            y,
            isRoutePoint
        )
    }
}