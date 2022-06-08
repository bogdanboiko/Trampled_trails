package com.example.gh_coursework.ui.point_details.mapper

import com.example.gh_coursework.domain.entity.PointsTagsDomain
import com.example.gh_coursework.ui.point_details.model.PointsTagsModel

fun mapPointsTagsModelToDomain(pointsTags: PointsTagsModel): PointsTagsDomain {
    return PointsTagsDomain(pointsTags.pointId, pointsTags.tagId)
}