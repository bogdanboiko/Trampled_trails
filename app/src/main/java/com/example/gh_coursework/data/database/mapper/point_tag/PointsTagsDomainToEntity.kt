package com.example.gh_coursework.data.database.mapper.point_tag

import com.example.gh_coursework.data.database.entity.PointsTagsEntity
import com.example.gh_coursework.domain.entity.PointsTagsDomain

fun mapPointsTagsDomainToEntity(pointsTags: PointsTagsDomain): PointsTagsEntity {
    return PointsTagsEntity(pointsTags.pointId, pointsTags.tagId)
}