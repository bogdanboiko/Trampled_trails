package com.example.gh_coursework.ui.private_point.mapper

import com.example.gh_coursework.domain.entity.PointDomain
import com.example.gh_coursework.domain.entity.PointPreviewDomain
import com.example.gh_coursework.ui.point_details.mapper.mapPointImageDomainToModel
import com.example.gh_coursework.ui.point_details.mapper.mapPointTagDomainToModel
import com.example.gh_coursework.ui.private_point.model.PrivatePointDetailsModel
import com.example.gh_coursework.ui.private_point.model.PrivatePointModel

fun mapPointDomainToModel(point: PointDomain): PrivatePointDetailsModel {
    return PrivatePointDetailsModel(
        point.pointId,
        point.x,
        point.y,
        point.imageList.map(::mapPointImageDomainToModel),
        point.tagList.map(::mapPointTagDomainToModel),
        point.caption,
        point.description
    )
}