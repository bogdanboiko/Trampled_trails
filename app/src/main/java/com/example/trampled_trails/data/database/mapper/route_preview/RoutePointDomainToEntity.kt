package com.example.trampled_trails.data.database.mapper.route_preview

import com.example.trampled_trails.data.database.entity.PointPreviewEntity
import com.example.trampled_trails.domain.entity.PointDomain

fun mapRoutePointDomainToEntity(point: PointDomain): PointPreviewEntity {
    with(point) {
        return PointPreviewEntity(
            pointId,
            x,
            y,
            point.routeId,
            isRoutePoint,
            point.position
        )
    }
}