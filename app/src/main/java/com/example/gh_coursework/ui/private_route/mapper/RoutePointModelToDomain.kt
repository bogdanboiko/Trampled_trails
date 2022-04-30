package com.example.gh_coursework.ui.private_route.mapper

import com.example.gh_coursework.domain.entity.PointDomain
import com.example.gh_coursework.ui.point_details.mapper.mapPointImageModelToDomain
import com.example.gh_coursework.ui.point_details.mapper.mapPointTagModelToDomain
import com.example.gh_coursework.ui.private_route.model.RoutePointModel

fun mapRoutePointModelToDomain(point: RoutePointModel): PointDomain {
    with(point) {
        return PointDomain(
            pointId,
            caption,
            description,
            tagList.map(::mapPointTagModelToDomain),
            imageList.map(::mapPointImageModelToDomain),
            x,
            y,
            routeId,
            isRoutePoint,
            position
        )
    }
}