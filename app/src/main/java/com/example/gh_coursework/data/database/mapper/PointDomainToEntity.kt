package com.example.gh_coursework.data.database.mapper

import com.example.gh_coursework.data.database.entity.PointCoordinatesEntity
import com.example.gh_coursework.domain.entity.PointPreviewDomain

fun mapPointDomainToEntity(point: PointPreviewDomain): PointCoordinatesEntity {
    return PointCoordinatesEntity(point.pointId, point.x, point.y)
}