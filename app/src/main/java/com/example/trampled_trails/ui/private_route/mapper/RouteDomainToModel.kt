package com.example.trampled_trails.ui.private_route.mapper

import com.example.trampled_trails.domain.entity.RouteDomain
import com.example.trampled_trails.ui.private_route.model.RouteModel
import com.example.trampled_trails.ui.route_details.mapper.mapRouteImageDomainToModel
import com.example.trampled_trails.ui.route_details.mapper.mapRouteTagDomainToModel

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