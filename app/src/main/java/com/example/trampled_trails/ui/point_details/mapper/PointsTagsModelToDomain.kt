package com.example.trampled_trails.ui.point_details.mapper

import com.example.trampled_trails.domain.entity.PointsTagsDomain
import com.example.trampled_trails.ui.point_details.model.PointsTagsModel

fun mapPointsTagsModelToDomain(pointsTags: PointsTagsModel): PointsTagsDomain {
    return PointsTagsDomain(pointsTags.pointId, pointsTags.tagId)
}