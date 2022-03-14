package com.example.gh_coursework.ui.private_point.mapper

import com.example.gh_coursework.domain.entity.PointDetailsDomain
import com.example.gh_coursework.ui.point_details.mapper.mapPointTagDomainToModel
import com.example.gh_coursework.ui.private_point.model.PrivatePointDetailsPreviewModel

fun mapPointDetailsDomainToModel(details: PointDetailsDomain?) : PrivatePointDetailsPreviewModel? {
    return if (details != null) {
        PrivatePointDetailsPreviewModel(details.tagList.map(::mapPointTagDomainToModel), details.caption, details.description)
    } else {
        null
    }
}