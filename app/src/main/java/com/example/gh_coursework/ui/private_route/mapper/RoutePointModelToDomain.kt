package com.example.gh_coursework.ui.private_route.mapper

import com.example.gh_coursework.domain.entity.RoutePointDomain
import com.example.gh_coursework.ui.point_details.mapper.mapPointImageModelToDomain
import com.example.gh_coursework.ui.point_details.mapper.mapPointTagModelToDomain
import com.example.gh_coursework.ui.private_route.model.RoutePointModel

fun mapRoutePointModelToDomain(point: RoutePointModel): RoutePointDomain {
    with(point) {
        return RoutePointDomain(
            pointId,
            caption,
            description,
            tagList.map(::mapPointTagModelToDomain),
            imageList.map(::mapPointImageModelToDomain),
            x,
            y,
            isRoutePoint
        )
    }
}