package com.example.gh_coursework.ui.route_details.mapper

import com.example.gh_coursework.domain.entity.RouteImageDomain
import com.example.gh_coursework.ui.model.ImageModel.RouteImageModel

fun mapRouteImageModelToDomain(image: RouteImageModel): RouteImageDomain {
    return RouteImageDomain(image.routeId, image.image)
}