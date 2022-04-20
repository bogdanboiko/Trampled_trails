package com.example.gh_coursework.ui.private_point.mapper

import com.example.gh_coursework.domain.entity.PointCompleteDetailsDomain
import com.example.gh_coursework.domain.entity.PointDetailsDomain
import com.example.gh_coursework.ui.point_details.mapper.mapPointImageDomainToModel
import com.example.gh_coursework.ui.point_details.mapper.mapPointTagDomainToModel
import com.example.gh_coursework.ui.private_point.model.PrivatePointDetailsModel

fun mapPointCompleteDetailsDomainToModel(details: PointCompleteDetailsDomain): PrivatePointDetailsModel {
        return PrivatePointDetailsModel(
            details.pointId,
            details.x,
            details.y,
            details.imageList.map(::mapPointImageDomainToModel),
            details.tagList.map(::mapPointTagDomainToModel),
            details.caption,
            details.description
        )
}