package com.example.gh_coursework.data.database.mapper

import com.example.gh_coursework.data.database.entity.PointCoordinatesEntity
import com.example.gh_coursework.domain.entity.PointPreviewDomain

fun mapRoutePointEntityToDomain(point: PointCoordinatesEntity): PointPreviewDomain {
    return  PointPreviewDomain(point.pointId, point.x, point.y, point.isRoutePoint)
}