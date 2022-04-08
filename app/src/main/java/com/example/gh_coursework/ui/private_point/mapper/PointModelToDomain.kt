package com.example.gh_coursework.ui.private_point.mapper

import com.example.gh_coursework.domain.entity.PointPreviewDomain
import com.example.gh_coursework.ui.private_point.model.PrivatePointModel

fun mapPointModelToDomain(point: PrivatePointModel): PointPreviewDomain {
    return PointPreviewDomain(point.pointId, point.x, point.y, false)
}