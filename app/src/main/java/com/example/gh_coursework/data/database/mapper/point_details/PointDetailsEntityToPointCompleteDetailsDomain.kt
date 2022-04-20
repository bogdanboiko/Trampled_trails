package com.example.gh_coursework.data.database.mapper.point_details

import com.example.gh_coursework.data.database.mapper.images.mapPointImageEntityToDomain
import com.example.gh_coursework.data.database.mapper.point_tag.mapPointTagEntityToDomain
import com.example.gh_coursework.data.database.response.PointDetailsResponse
import com.example.gh_coursework.domain.entity.PointCompleteDetailsDomain

fun mapPointDetailsEntityToPointCompleteDetailsDomain(details: PointDetailsResponse): PointCompleteDetailsDomain {
    with(details) {
        return PointCompleteDetailsDomain(
            pointDetails.pointId,
            coordinate.x,
            coordinate.y,
            tagList.map(::mapPointTagEntityToDomain),
            imageList.map(::mapPointImageEntityToDomain),
            pointDetails.caption,
            pointDetails.description
        )
    }
}