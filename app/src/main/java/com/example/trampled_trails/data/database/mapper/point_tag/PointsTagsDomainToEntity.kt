package com.example.trampled_trails.data.database.mapper.point_tag

import com.example.trampled_trails.data.database.entity.PointsTagsEntity
import com.example.trampled_trails.domain.entity.PointsTagsDomain

fun mapPointsTagsDomainToEntity(pointsTags: PointsTagsDomain): PointsTagsEntity {
    return PointsTagsEntity(pointsTags.pointId, pointsTags.tagId)
}