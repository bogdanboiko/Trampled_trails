package com.example.gh_coursework.ui.private_route.mapper

import com.example.gh_coursework.domain.entity.PointDetailsDomain
import com.example.gh_coursework.ui.point_details.mapper.mapPointTagDomainToModel
import com.example.gh_coursework.ui.private_route.model.PrivateRoutePointDetailsPreviewModel

fun mapRoutePointDetailsDomainToModel(details: PointDetailsDomain?): PrivateRoutePointDetailsPreviewModel? {
    return if (details != null) {
        PrivateRoutePointDetailsPreviewModel(details.tagList.map(::mapPointTagDomainToModel), details.caption, details.description, 0.0, 0.0)
    } else {
        null
    }
}