package com.example.gh_coursework.ui.route_details.mapper

import com.example.gh_coursework.domain.entity.RoutePointsImagesDomain
import com.example.gh_coursework.ui.point_details.mapper.mapPointImageDomainToModel
import com.example.gh_coursework.ui.route_details.model.RoutePointsImagesModel


fun mapRoutePointsImagesDomainToModel(images: RoutePointsImagesDomain): RoutePointsImagesModel {
    return RoutePointsImagesModel(images.imagesList.map(::mapPointImageDomainToModel))
}