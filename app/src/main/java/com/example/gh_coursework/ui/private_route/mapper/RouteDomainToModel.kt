package com.example.gh_coursework.ui.private_route.mapper

import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.ui.private_route.model.RouteModel
import com.example.gh_coursework.ui.route_details.mapper.mapRouteImageDomainToModel
import com.example.gh_coursework.ui.route_details.mapper.mapRouteTagDomainToModel

fun mapRouteDomainToModel(routeDomain: RouteDomain): RouteModel {
    with(routeDomain) {
        return RouteModel(
            routeId,
            name,
            description,
            tagsList.map(::mapRouteTagDomainToModel),
            imageList.map(::mapRouteImageDomainToModel),
            isPublic
        )
    }
}