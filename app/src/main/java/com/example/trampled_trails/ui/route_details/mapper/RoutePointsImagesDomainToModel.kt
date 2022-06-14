package com.example.trampled_trails.ui.route_details.mapper

import com.example.trampled_trails.domain.entity.RoutePointsImagesDomain
import com.example.trampled_trails.ui.point_details.mapper.mapPointImageDomainToModel
import com.example.trampled_trails.ui.route_details.model.RoutePointsImagesModel

fun mapRoutePointsImagesDomainToModel(images: RoutePointsImagesDomain): RoutePointsImagesModel {
    return RoutePointsImagesModel(images.imagesList.map(::mapPointImageDomainToModel))
}