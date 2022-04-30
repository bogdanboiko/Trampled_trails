package com.example.gh_coursework.data.database.mapper.route_preview

import com.example.gh_coursework.data.database.entity.PointCoordinatesEntity
import com.example.gh_coursework.domain.entity.PointDomain

fun mapRoutePointDomainToEntity(point: PointDomain): PointCoordinatesEntity {
    with(point) {
        return PointCoordinatesEntity(
            pointId,
            x,
            y,
            point.routeId,
            isRoutePoint,
            point.position
        )
    }
}