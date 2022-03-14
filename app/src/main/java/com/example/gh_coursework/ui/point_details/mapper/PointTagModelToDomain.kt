package com.example.gh_coursework.ui.point_details.mapper

import com.example.gh_coursework.domain.entity.PointTagDomain
import com.example.gh_coursework.ui.point_details.model.PointTagModel

fun mapPointTagModelToDomain(tag: PointTagModel): PointTagDomain {
    return PointTagDomain(tag.tagId, tag.name)
}