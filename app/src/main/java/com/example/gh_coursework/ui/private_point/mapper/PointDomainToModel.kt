package com.example.gh_coursework.ui.private_point.mapper

import com.example.gh_coursework.domain.entity.PointPreviewDomain
import com.example.gh_coursework.ui.private_point.model.PrivatePointModel

fun mapPointDomainToModel(point: PointPreviewDomain): PrivatePointModel {
    return PrivatePointModel(point.pointId, point.x, point.y, point.isRoutePoint)
}