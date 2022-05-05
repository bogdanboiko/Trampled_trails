package com.example.gh_coursework.data.database.mapper.route_preview

import com.example.gh_coursework.data.database.entity.PointPreviewEntity
import com.example.gh_coursework.domain.entity.PointDomain

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