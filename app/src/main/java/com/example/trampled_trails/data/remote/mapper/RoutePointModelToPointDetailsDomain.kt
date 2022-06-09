package com.example.trampled_trails.data.remote.mapper

import com.example.trampled_trails.domain.entity.PointDetailsDomain
import com.example.trampled_trails.ui.point_details.mapper.mapPointImageModelToDomain
import com.example.trampled_trails.ui.point_details.mapper.mapPointTagModelToDomain
import com.example.trampled_trails.ui.private_route.model.RoutePointModel

fun mapRoutePointModelToPointDetailsDomain(point: RoutePointModel): PointDetailsDomain {
    return PointDetailsDomain(
        point.pointId,
        point.tagList.map(::mapPointTagModelToDomain),
        point.imageList.map(::mapPointImageModelToDomain),
        point.caption,
        point.description
    )
}