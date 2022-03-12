package com.example.gh_coursework.ui.private_route.mapper

import com.example.gh_coursework.domain.entity.PointDetailsDomain
import com.example.gh_coursework.ui.private_route.model.PrivateRoutePointDetailsPreviewModel

fun mapRoutePointDetailsDomainToModel(details: PointDetailsDomain?): PrivateRoutePointDetailsPreviewModel? {
    return if (details != null) {
        PrivateRoutePointDetailsPreviewModel(details.tag, details.caption, details.description)
    } else {
        null
    }
}