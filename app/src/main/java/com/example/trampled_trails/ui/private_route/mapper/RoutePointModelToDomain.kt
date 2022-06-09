package com.example.trampled_trails.ui.private_route.mapper

import com.example.trampled_trails.domain.entity.PointPreviewDomain
import com.example.trampled_trails.ui.private_route.model.RoutePointModel

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