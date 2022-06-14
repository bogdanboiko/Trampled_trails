package com.example.trampled_trails.data.database.mapper.route_details

import com.example.trampled_trails.data.database.mapper.images.mapPointImageEntityToDomain
import com.example.trampled_trails.data.database.response.RoutePointImageResponse
import com.example.trampled_trails.domain.entity.RoutePointsImagesDomain

fun mapRoutePointsImagesResponseToDomain(response: RoutePointImageResponse): RoutePointsImagesDomain {
    return RoutePointsImagesDomain(response.pointImages.map(::mapPointImageEntityToDomain))
}