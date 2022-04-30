package com.example.gh_coursework.ui.route_details.mapper

import com.example.gh_coursework.domain.entity.RouteImageDomain
import com.example.gh_coursework.ui.private_image_details.model.ImageModel.RouteImageModel

fun mapRouteImageDomainToModel(image: RouteImageDomain): RouteImageModel {
    return RouteImageModel(image.routeId, image.image, image.isUploaded)
}