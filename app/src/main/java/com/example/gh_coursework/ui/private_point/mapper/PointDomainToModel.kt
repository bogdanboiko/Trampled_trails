package com.example.gh_coursework.ui.private_point.mapper

import com.example.gh_coursework.domain.entity.PointDomain
import com.example.gh_coursework.ui.point_details.mapper.mapPointImageDomainToModel
import com.example.gh_coursework.ui.point_details.mapper.mapPointTagDomainToModel
import com.example.gh_coursework.ui.private_point.model.PrivatePointDetailsModel

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