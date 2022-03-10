package com.example.gh_coursework.ui.private_point.mapper

import com.example.gh_coursework.domain.entity.PointDetailsDomain
import com.example.gh_coursework.ui.private_point.model.PrivatePointDetailsPreviewModel

fun mapPointDetailsDomainToModel(details: PointDetailsDomain?) : PrivatePointDetailsPreviewModel? {
    return if (details != null) {
        PrivatePointDetailsPreviewModel(details.tag, details.caption, details.description)
    } else {
        null
    }
}