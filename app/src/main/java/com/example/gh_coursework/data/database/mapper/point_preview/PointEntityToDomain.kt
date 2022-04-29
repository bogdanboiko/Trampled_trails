package com.example.gh_coursework.data.database.mapper.point_preview

import com.example.gh_coursework.data.database.entity.PointCoordinatesEntity
import com.example.gh_coursework.domain.entity.PointPreviewDomain

fun mapPointEntityToDomain(point: PointCoordinatesEntity): PointPreviewDomain {
    return PointPreviewDomain(point.pointId, point.x, point.y, point.routeId, point.isRoutePoint)
}