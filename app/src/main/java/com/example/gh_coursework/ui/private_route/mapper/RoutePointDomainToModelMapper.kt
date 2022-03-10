package com.example.gh_coursework.ui.private_route.mapper

import com.example.gh_coursework.domain.entity.PointPreviewDomain
import com.example.gh_coursework.ui.private_route.model.PrivateRoutePointModel

fun mapRoutePointDomainToModel(point: PointPreviewDomain): PrivateRoutePointModel {
    return PrivateRoutePointModel(point.pointId, point.x, point.y)
}