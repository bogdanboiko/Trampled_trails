package com.example.gh_coursework.ui.public_route.mapper

import com.example.gh_coursework.domain.entity.RoutePointDomain
import com.example.gh_coursework.ui.point_details.mapper.mapPointImageDomainToModel
import com.example.gh_coursework.ui.point_details.mapper.mapPointTagDomainToModel
import com.example.gh_coursework.ui.public_route.model.RoutePointModel

fun mapRoutePointDomainToModel(point: RoutePointDomain): RoutePointModel {
    with(point) {
        return RoutePointModel(
            pointId,
            caption,
            description,
            tagList.map(::mapPointTagDomainToModel),
            imageList.map(::mapPointImageDomainToModel),
            x,
            y,
            isRoutePoint
        )
    }
}