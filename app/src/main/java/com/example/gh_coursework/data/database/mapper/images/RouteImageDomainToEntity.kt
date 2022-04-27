package com.example.gh_coursework.data.database.mapper.images

import com.example.gh_coursework.data.database.entity.RouteImageEntity
import com.example.gh_coursework.domain.entity.RouteImageDomain

fun mapRouteImageDomainToEntity(image: RouteImageDomain): RouteImageEntity {
    return RouteImageEntity(image.routeId, image.image, image.isUploaded)
}