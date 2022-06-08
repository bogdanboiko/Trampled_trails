package com.example.gh_coursework.ui.private_route.mapper

import com.example.gh_coursework.domain.entity.PointPreviewDomain
import com.example.gh_coursework.ui.private_route.model.RoutePointModel

fun mapRoutePointModelToDomain(point: RoutePointModel): PointPreviewDomain {
    with(point) {
        return PointPreviewDomain(
            pointId,
            x,
            y,
            routeId,
            isRoutePoint,
            position
        )
    }
}