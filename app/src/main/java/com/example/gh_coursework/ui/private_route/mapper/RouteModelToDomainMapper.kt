package com.example.gh_coursework.ui.private_route.mapper

import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.ui.private_route.model.PrivateRouteModel

fun mapRouteModelToDomainMapper(route: PrivateRouteModel): RouteDomain {
    return RouteDomain(
        route.routeId,
        route.name,
        route.description,
        route.rating,
        route.coordinatesList.map(::mapPointModelToDomain)
    )
}