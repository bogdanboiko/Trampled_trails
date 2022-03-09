package com.example.gh_coursework.data.database.mapper

import com.example.gh_coursework.data.database.entity.PointCoordinatesEntity
import com.example.gh_coursework.domain.entity.PointPreviewDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun mapPointDomainToEntity(point: Flow<PointPreviewDomain>): Flow<PointCoordinatesEntity> {
    return point.map { PointCoordinatesEntity(it.pointId, it.x, it.y) }
}