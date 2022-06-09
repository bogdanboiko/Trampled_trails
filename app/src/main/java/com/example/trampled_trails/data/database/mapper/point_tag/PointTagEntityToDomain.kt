package com.example.trampled_trails.data.database.mapper.point_tag

import com.example.trampled_trails.data.database.entity.PointTagEntity
import com.example.trampled_trails.domain.entity.PointTagDomain

fun mapPointTagEntityToDomain(tag: PointTagEntity): PointTagDomain {
    return PointTagDomain(tag.tagId, tag.name)
}