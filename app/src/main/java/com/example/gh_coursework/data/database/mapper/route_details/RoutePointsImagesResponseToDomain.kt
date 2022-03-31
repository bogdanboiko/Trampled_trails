package com.example.gh_coursework.data.database.mapper.route_details

import com.example.gh_coursework.data.database.mapper.images.mapPointImageEntityToDomain
import com.example.gh_coursework.data.database.response.RoutePointImageResponse
import com.example.gh_coursework.domain.entity.RoutePointsImagesDomain

fun mapRoutePointsImagesResponseToDomain(response: RoutePointImageResponse): RoutePointsImagesDomain {
    return RoutePointsImagesDomain(response.pointImages.map(::mapPointImageEntityToDomain))
}