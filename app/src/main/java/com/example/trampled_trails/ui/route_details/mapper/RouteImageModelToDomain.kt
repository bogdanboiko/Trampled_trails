package com.example.trampled_trails.ui.route_details.mapper

import com.example.trampled_trails.domain.entity.RouteImageDomain
import com.example.trampled_trails.ui.private_image_details.model.ImageModel.RouteImageModel

fun mapRouteImageModelToDomain(image: RouteImageModel): RouteImageDomain {
    return RouteImageDomain(image.routeId, image.image, image.isUploaded)
}