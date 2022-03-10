package com.example.gh_coursework.data.database.mapper

import com.example.gh_coursework.data.database.entity.PointDetailsEntity
import com.example.gh_coursework.domain.entity.PointDetailsDomain

fun mapPointDetailsEntityToDomain(details: PointDetailsEntity): PointDetailsDomain {
    return PointDetailsDomain(details.pointId, details.tag, details.caption, details.description)
}