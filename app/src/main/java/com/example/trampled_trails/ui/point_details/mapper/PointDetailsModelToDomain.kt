package com.example.trampled_trails.ui.point_details.mapper

import com.example.trampled_trails.domain.entity.PointDetailsDomain
import com.example.trampled_trails.ui.point_details.model.PointDetailsModel

fun mapPointDetailsModelToDomain(details: PointDetailsModel): PointDetailsDomain {
    return PointDetailsDomain(
        details.pointId,
        details.tagList.map(::mapPointTagModelToDomain),
        details.imageList.map(::mapPointImageModelToDomain),
        details.caption,
        details.description
    )
}