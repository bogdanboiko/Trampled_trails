package com.example.gh_coursework.data.remote.mapper

import com.example.gh_coursework.domain.entity.PointDetailsDomain
import com.example.gh_coursework.ui.point_details.mapper.mapPointImageModelToDomain
import com.example.gh_coursework.ui.point_details.mapper.mapPointTagModelToDomain
import com.example.gh_coursework.ui.private_route.model.RoutePointModel

fun mapRoutePointModelToPointDetailsDomain(point: RoutePointModel): PointDetailsDomain {
    return PointDetailsDomain(
        point.pointId,
        point.tagList.map(::mapPointTagModelToDomain),
        point.imageList.map(::mapPointImageModelToDomain),
        point.caption,
        point.description
    )
}