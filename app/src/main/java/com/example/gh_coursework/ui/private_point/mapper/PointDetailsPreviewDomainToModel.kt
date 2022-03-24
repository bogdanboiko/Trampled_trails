package com.example.gh_coursework.ui.private_point.mapper

import com.example.gh_coursework.domain.entity.PointDetailsDomain
import com.example.gh_coursework.ui.point_details.mapper.mapPointImageDomainToModel
import com.example.gh_coursework.ui.point_details.mapper.mapPointTagDomainToModel
import com.example.gh_coursework.ui.private_point.model.PrivatePointDetailsPreviewModel

fun mapPointDetailsDomainToModel(details: PointDetailsDomain): PrivatePointDetailsPreviewModel {
    return PrivatePointDetailsPreviewModel(
        details.pointId,
        details.imageList.map(::mapPointImageDomainToModel),
        details.tagList.map(::mapPointTagDomainToModel),
        details.caption,
        details.description
    )
}