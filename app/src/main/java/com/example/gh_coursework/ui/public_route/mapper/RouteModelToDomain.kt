package com.example.gh_coursework.ui.public_route.mapper

import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.ui.public_route.model.RouteModel
import com.example.gh_coursework.ui.route_details.mapper.mapRouteImageModelToDomain
import com.example.gh_coursework.ui.route_details.mapper.mapRouteTagModelToDomain

fun mapRouteModelToDomain(route: RouteModel): RouteDomain {
    with(route) {
        return RouteDomain(
            routeId,
            name,
            description,
            rating,
            tagsList.map(::mapRouteTagModelToDomain),
            imageList.map(::mapRouteImageModelToDomain)
        )
    }
}