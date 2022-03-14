package com.example.gh_coursework.data.database.mapper

import com.example.gh_coursework.data.database.entity.PointTagEntity
import com.example.gh_coursework.domain.entity.PointTagDomain

fun mapTagDomainToEntity(tag: PointTagDomain): PointTagEntity {
    return PointTagEntity(tag.tagId, tag.name)
}