package com.example.gh_coursework.data.database.mapper.point_tag

import com.example.gh_coursework.data.database.entity.PointTagEntity
import com.example.gh_coursework.domain.entity.PointTagDomain

fun mapPointTagEntityToDomain(tag: PointTagEntity): PointTagDomain {
    return PointTagDomain(tag.tagId, tag.name)
}