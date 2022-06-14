package com.example.trampled_trails.ui.private_route.mapper

import com.example.trampled_trails.domain.entity.RouteDomain
import com.example.trampled_trails.ui.private_route.model.RouteModel
import com.example.trampled_trails.ui.route_details.mapper.mapRouteImageModelToDomain
import com.example.trampled_trails.ui.route_details.mapper.mapRouteTagModelToDomain

fun mapRouteModelToDomain(route: RouteModel): RouteDomain {
    with(route) {
        return RouteDomain(
            routeId,
            name,
            description,
            tagsList.map(::mapRouteTagModelToDomain),
            imageList.map(::mapRouteImageModelToDomain),
            isPublic
        )
    }
}