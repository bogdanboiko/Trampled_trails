package com.example.gh_coursework.data.database.mapper.route_preview

import com.example.gh_coursework.data.database.entity.PointCoordinatesEntity
import com.example.gh_coursework.domain.entity.RoutePointDomain

fun mapRoutePointDomainToEntity(point: RoutePointDomain): PointCoordinatesEntity {
    with(point) {
        return PointCoordinatesEntity(
            pointId,
            x,
            y,
            isRoutePoint
        )
    }
}