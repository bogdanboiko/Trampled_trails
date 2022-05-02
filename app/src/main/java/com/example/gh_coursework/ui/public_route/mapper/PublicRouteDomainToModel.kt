package com.example.gh_coursework.ui.public_route.mapper

import com.example.gh_coursework.domain.entity.PublicRouteDomain
import com.example.gh_coursework.ui.public_route.model.PublicRouteModel

fun mapPublicRouteDomainToModel(route: PublicRouteDomain): PublicRouteModel {
    with(route) {
        return PublicRouteModel(routeId, name, description, tagsList, imageList, userId, isPublic)
    }
}