package com.example.gh_coursework.data.database.mapper

import com.example.gh_coursework.data.database.entity.PointDetailsEntity
import com.example.gh_coursework.domain.entity.PointDetailsDomain

fun mapPointDetailsDomainToEntity(details: PointDetailsDomain): PointDetailsEntity {
    return PointDetailsEntity(details.pointId, details.caption, details.description)
}