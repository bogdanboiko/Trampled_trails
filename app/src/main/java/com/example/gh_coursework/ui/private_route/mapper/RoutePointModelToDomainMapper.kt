package com.example.gh_coursework.ui.private_route.mapper

import com.example.gh_coursework.domain.entity.PointPreviewDomain
import com.example.gh_coursework.ui.private_route.model.PrivateRoutePointModel

fun mapRoutePointModelToDomain(point: PrivateRoutePointModel): PointPreviewDomain {
    return PointPreviewDomain(point.pointId, point.x, point.y)
}