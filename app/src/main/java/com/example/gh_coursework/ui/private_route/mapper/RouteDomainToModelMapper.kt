package com.example.gh_coursework.ui.private_route.mapper

import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.ui.private_route.model.PrivateRouteModel
import com.example.gh_coursework.ui.private_route.model.PrivateRoutePointModel

fun mapRouteDomainToModel(routeDomain: RouteDomain): PrivateRouteModel {
    return PrivateRouteModel(
        routeDomain.routeId,
        routeDomain.name,
        routeDomain.description,
        routeDomain.rating,
        routeDomain.coordinatesList.map(::mapPointDomainToModel),
        null
    )
}