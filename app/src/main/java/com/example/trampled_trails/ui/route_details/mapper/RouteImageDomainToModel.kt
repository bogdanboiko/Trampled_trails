package com.example.trampled_trails.ui.route_details.mapper

import com.example.trampled_trails.domain.entity.RouteImageDomain
import com.example.trampled_trails.ui.private_image_details.model.ImageModel.RouteImageModel

fun mapRouteImageDomainToModel(image: RouteImageDomain): RouteImageModel {
    return RouteImageModel(image.routeId, image.image, image.isUploaded)
}