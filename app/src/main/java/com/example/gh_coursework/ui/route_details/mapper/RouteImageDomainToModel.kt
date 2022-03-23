package com.example.gh_coursework.ui.route_details.mapper

import com.example.gh_coursework.domain.entity.RouteImageDomain
import com.example.gh_coursework.ui.route_details.model.RouteImageModel

fun mapRouteImageDomainToModel(image: RouteImageDomain): RouteImageModel {
    return RouteImageModel(image.routeId, image.image)
}