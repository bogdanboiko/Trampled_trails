package com.example.trampled_trails.data.database.mapper.point_details

import com.example.trampled_trails.data.database.mapper.images.mapPointImageEntityToDomain
import com.example.trampled_trails.data.database.mapper.point_tag.mapPointTagEntityToDomain
import com.example.trampled_trails.data.database.response.PointResponse
import com.example.trampled_trails.domain.entity.PointDetailsDomain

fun mapPointDetailsEntityToDomain(details: PointResponse): PointDetailsDomain {
    return with(details.pointDetails) {
        PointDetailsDomain(
            pointId,
            details.tagList.map(::mapPointTagEntityToDomain),
            details.imageList.map(::mapPointImageEntityToDomain),
            caption,
            description
        )
    }
}