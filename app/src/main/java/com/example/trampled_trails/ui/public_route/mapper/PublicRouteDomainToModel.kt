package com.example.trampled_trails.ui.public_route.mapper

import com.example.trampled_trails.domain.entity.PublicRouteDomain
import com.example.trampled_trails.ui.public_route.model.PublicRouteModel

fun mapPublicRouteDomainToModel(route: PublicRouteDomain): PublicRouteModel {
    with(route) {
        return PublicRouteModel(routeId, name, description, tagsList, imageList, userId, isPublic)
    }
}