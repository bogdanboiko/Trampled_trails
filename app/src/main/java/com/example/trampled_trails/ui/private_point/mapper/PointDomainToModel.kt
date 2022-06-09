package com.example.trampled_trails.ui.private_point.mapper

import com.example.trampled_trails.domain.entity.PointDomain
import com.example.trampled_trails.ui.point_details.mapper.mapPointImageDomainToModel
import com.example.trampled_trails.ui.point_details.mapper.mapPointTagDomainToModel
import com.example.trampled_trails.ui.private_point.model.PrivatePointDetailsModel

fun mapPointDomainToModel(point: PointDomain): PrivatePointDetailsModel {
    return PrivatePointDetailsModel(
        point.pointId,
        point.x,
        point.y,
        point.imageList.map(::mapPointImageDomainToModel),
        point.tagsList.map(::mapPointTagDomainToModel),
        point.caption,
        point.description,
        point.routeId
    )
}