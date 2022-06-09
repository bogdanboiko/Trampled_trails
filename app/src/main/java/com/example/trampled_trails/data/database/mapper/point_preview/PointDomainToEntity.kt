package com.example.trampled_trails.data.database.mapper.point_preview

import com.example.trampled_trails.data.database.entity.PointPreviewEntity
import com.example.trampled_trails.domain.entity.PointPreviewDomain

fun mapPointDomainToEntity(point: PointPreviewDomain): PointPreviewEntity {
    return PointPreviewEntity(point.pointId, point.x, point.y, point.routeId, point.isRoutePoint, null)
}