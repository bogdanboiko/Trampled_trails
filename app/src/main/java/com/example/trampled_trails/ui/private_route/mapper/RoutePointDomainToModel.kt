package com.example.trampled_trails.ui.private_route.mapper

import com.example.trampled_trails.domain.entity.PointDomain
import com.example.trampled_trails.ui.point_details.mapper.mapPointImageDomainToModel
import com.example.trampled_trails.ui.point_details.mapper.mapPointTagDomainToModel
import com.example.trampled_trails.ui.private_route.model.RoutePointModel

fun mapRoutePointDomainToModel(point: PointDomain): RoutePointModel {
    with(point) {
        return RoutePointModel(
            pointId,
            caption,
            description,
            tagsList.map(::mapPointTagDomainToModel),
            imageList.map(::mapPointImageDomainToModel),
            x,
            y,
            point.routeId,
            isRoutePoint,
            point.position
        )
    }
}