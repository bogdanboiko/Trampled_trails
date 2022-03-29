package com.example.gh_coursework.ui.private_route.mapper

import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.ui.private_route.model.PrivateRouteModel
import com.example.gh_coursework.ui.route_details.mapper.mapRouteImageDomainToModel
import com.example.gh_coursework.ui.route_details.mapper.mapRouteTagDomainToModel

fun mapRouteDomainToModel(routeDomain: RouteDomain): PrivateRouteModel {
    with(routeDomain) {
        return PrivateRouteModel(
            routeId,
            name,
            description,
            rating,
            points.map(::mapRoutePointDetailsDomainToModel),
            tagsList.map(::mapRouteTagDomainToModel),
            imageList.map(::mapRouteImageDomainToModel),
            coordinates.map(::mapPointDomainToModel)
        )
    }
}