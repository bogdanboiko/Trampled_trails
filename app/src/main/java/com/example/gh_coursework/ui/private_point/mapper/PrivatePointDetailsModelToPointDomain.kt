package com.example.gh_coursework.ui.private_point.mapper

import com.example.gh_coursework.domain.entity.PointDetailsDomain
import com.example.gh_coursework.ui.point_details.mapper.mapPointImageModelToDomain
import com.example.gh_coursework.ui.point_details.mapper.mapPointTagModelToDomain
import com.example.gh_coursework.ui.private_point.model.PrivatePointDetailsModel

fun mapPrivatePointDetailsModelToPointDomain(point: PrivatePointDetailsModel): PointDetailsDomain {
    return PointDetailsDomain(
        point.pointId,
        point.tagList.map(::mapPointTagModelToDomain),
        point.imageList.map(::mapPointImageModelToDomain),
        point.caption,
        point.description
    )
}