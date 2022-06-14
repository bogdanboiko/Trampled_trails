package com.example.trampled_trails.data.database.mapper.point_details

import com.example.trampled_trails.data.database.entity.PointDetailsEntity
import com.example.trampled_trails.domain.entity.PointDetailsDomain

fun mapPointDetailsDomainToEntity(details: PointDetailsDomain): PointDetailsEntity {
    return PointDetailsEntity(details.pointId, details.caption, details.description)
}