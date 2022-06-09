package com.example.trampled_trails.ui.public_route.mapper

import com.example.trampled_trails.domain.entity.PublicPointDomain
import com.example.trampled_trails.ui.public_route.model.RoutePointModel

fun mapRoutePointDomainToModel(point: PublicPointDomain): RoutePointModel {
    with(point) {
        return RoutePointModel(
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