package com.example.gh_coursework.ui.point_details.mapper

import com.example.gh_coursework.domain.entity.PointDetailsDomain
import com.example.gh_coursework.ui.point_details.model.PointDetailsModel

fun mapPointDetailsDomainToModel(details: PointDetailsDomain?): PointDetailsModel? {
    return if (details != null) {
        PointDetailsModel(details.pointId, details.tag, details.caption, details.description)
    } else {
        null
    }
}