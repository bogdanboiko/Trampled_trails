package com.example.gh_coursework.data.database.mapper.point_details

import com.example.gh_coursework.data.database.mapper.images.mapPointImageEntityToDomain
import com.example.gh_coursework.data.database.mapper.point_tag.mapPointTagEntityToDomain
import com.example.gh_coursework.data.database.response.PointResponse
import com.example.gh_coursework.domain.entity.PointDomain

fun mapPointResponseToDomain(point: PointResponse): PointDomain {
    with(point) {
        return PointDomain(
            coordinate.pointId,
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