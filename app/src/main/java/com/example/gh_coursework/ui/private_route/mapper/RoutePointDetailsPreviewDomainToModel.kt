package com.example.gh_coursework.ui.private_route.mapper

import com.example.gh_coursework.domain.entity.PointDetailsDomain
import com.example.gh_coursework.ui.point_details.mapper.mapPointImageDomainToModel
import com.example.gh_coursework.ui.point_details.mapper.mapPointTagDomainToModel
import com.example.gh_coursework.ui.private_route.model.PrivateRoutePointDetailsPreviewModel

fun mapRoutePointDetailsDomainToModel(details: PointDetailsDomain?): PrivateRoutePointDetailsPreviewModel? {
    if (details != null) {
        return PrivateRoutePointDetailsPreviewModel(
            details.pointId,
            details.imageList.map(::mapPointImageDomainToModel),
            details.tagList.map(::mapPointTagDomainToModel),
            details.caption,
            details.description
        )
    }

    return null
}