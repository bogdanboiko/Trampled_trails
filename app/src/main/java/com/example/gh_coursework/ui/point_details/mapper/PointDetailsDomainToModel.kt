package com.example.gh_coursework.ui.point_details.mapper

import com.example.gh_coursework.domain.entity.PointDetailsDomain
import com.example.gh_coursework.ui.point_details.model.PointDetailsModel

fun mapPointDetailsDomainToModel(details: PointDetailsDomain): PointDetailsModel {
    return PointDetailsModel(
        details.pointId,
        details.tagList.map(::mapPointTagDomainToModel),
        details.imageList.map(::mapPointImageDomainToModel),
        details.caption,
        details.description
    )
}