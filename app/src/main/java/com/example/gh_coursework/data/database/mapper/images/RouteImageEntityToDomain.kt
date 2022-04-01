package com.example.gh_coursework.data.database.mapper.images

import com.example.gh_coursework.data.database.entity.RouteImageEntity
import com.example.gh_coursework.domain.entity.RouteImageDomain

fun mapRouteImageEntityToDomain(image: RouteImageEntity): RouteImageDomain {
    return RouteImageDomain(image.routeId, image.image)
}