package com.example.gh_coursework.ui.private_route.mapper

import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.ui.private_route.model.RouteModel

fun mapRouteModelToDomainMapper(route: RouteModel): RouteDomain {
    with(route) {
        return RouteDomain(
            routeId,
            name,
            description,
            rating,
            emptyList(),
            emptyList()
        )
    }
}