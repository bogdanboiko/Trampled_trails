package com.example.gh_coursework.data.database.mapper.point_details

import com.example.gh_coursework.data.database.mapper.images.mapPointImageEntityToDomain
import com.example.gh_coursework.data.database.mapper.point_tag.mapPointTagEntityToDomain
import com.example.gh_coursework.data.database.response.PointResponse
import com.example.gh_coursework.domain.entity.PointDetailsDomain

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