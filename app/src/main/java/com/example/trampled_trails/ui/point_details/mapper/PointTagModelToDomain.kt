package com.example.trampled_trails.ui.point_details.mapper

import com.example.trampled_trails.domain.entity.PointTagDomain
import com.example.trampled_trails.ui.point_details.model.PointTagModel

fun mapPointTagModelToDomain(tag: PointTagModel): PointTagDomain {
    return PointTagDomain(tag.tagId, tag.name)
}