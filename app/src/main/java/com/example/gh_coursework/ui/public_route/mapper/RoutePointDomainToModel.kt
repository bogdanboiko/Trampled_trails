package com.example.gh_coursework.ui.public_route.mapper

import com.example.gh_coursework.domain.entity.PublicPointDomain
import com.example.gh_coursework.ui.public_route.model.RoutePointModel

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