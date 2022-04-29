package com.example.gh_coursework.data.database.mapper.route_preview

import com.example.gh_coursework.data.database.mapper.images.mapPointImageEntityToDomain
import com.example.gh_coursework.data.database.mapper.point_tag.mapPointTagEntityToDomain
import com.example.gh_coursework.data.database.response.RoutePointsResponse
import com.example.gh_coursework.domain.entity.RoutePointDomain

fun mapRoutePointEntityToDomain(routePointsResponse: RoutePointsResponse): RoutePointDomain {
    with(routePointsResponse) {
        return RoutePointDomain(
            pointDetails.pointId,
            pointDetails.caption,
            pointDetails.description,
            tagList.map(::mapPointTagEntityToDomain),
            imageList.map(::mapPointImageEntityToDomain),
            coordinate.x,
            coordinate.y,
            coordinate.routeId,
            coordinate.isRoutePoint
        )
    }
}