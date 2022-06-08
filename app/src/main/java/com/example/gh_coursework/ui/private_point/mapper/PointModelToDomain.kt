package com.example.gh_coursework.ui.private_point.mapper

import com.example.gh_coursework.domain.entity.PointPreviewDomain
import com.example.gh_coursework.ui.private_point.model.PrivatePointDetailsModel

fun mapPointModelToDomain(point: PrivatePointDetailsModel): PointPreviewDomain {
    return PointPreviewDomain(point.pointId, point.x, point.y, null, false, null)
}