package com.example.trampled_trails.ui.private_point.mapper

import com.example.trampled_trails.domain.entity.PointPreviewDomain
import com.example.trampled_trails.ui.private_point.model.PrivatePointDetailsModel

fun mapPointModelToDomain(point: PrivatePointDetailsModel): PointPreviewDomain {
    return PointPreviewDomain(point.pointId, point.x, point.y, null, false, null)
}