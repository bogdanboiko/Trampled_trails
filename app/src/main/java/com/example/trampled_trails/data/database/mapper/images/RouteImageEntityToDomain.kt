package com.example.trampled_trails.data.database.mapper.images

import com.example.trampled_trails.data.database.entity.RouteImageEntity
import com.example.trampled_trails.domain.entity.RouteImageDomain

fun mapRouteImageEntityToDomain(image: RouteImageEntity): RouteImageDomain {
    return RouteImageDomain(image.routeId, image.image, image.isUploaded)
}