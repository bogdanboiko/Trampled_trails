package com.example.gh_coursework.ui.point_details.mapper

import com.example.gh_coursework.domain.entity.PointTagDomain
import com.example.gh_coursework.ui.point_details.model.PointTagModel

fun mapPointTagDomainToModel(tag: PointTagDomain): PointTagModel {
    return PointTagModel(tag.tagId, tag.name)
}