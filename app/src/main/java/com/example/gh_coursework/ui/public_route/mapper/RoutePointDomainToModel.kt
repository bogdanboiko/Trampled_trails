package com.example.gh_coursework.ui.public_route.mapper

import com.example.gh_coursework.domain.entity.PublicRoutePointDomain
import com.example.gh_coursework.domain.entity.RoutePointDomain
import com.example.gh_coursework.ui.point_details.mapper.mapPointImageDomainToModel
import com.example.gh_coursework.ui.point_details.mapper.mapPointTagDomainToModel
import com.example.gh_coursework.ui.public_route.model.RoutePointModel

fun mapRoutePointDomainToModel(point: PublicRoutePointDomain): RoutePointModel {
    with(point) {
        return RoutePointModel(
            pointId,
            caption,
            description,
            imageList,
            x,
            y,
            isRoutePoint
        )
    }
}