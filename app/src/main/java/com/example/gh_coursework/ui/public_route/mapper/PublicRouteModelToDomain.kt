package com.example.gh_coursework.ui.public_route.mapper

import com.example.gh_coursework.domain.entity.PublicRouteDomain
import com.example.gh_coursework.ui.public_route.model.PublicRouteModel

fun mapPublicRouteModelToDomain(route: PublicRouteModel): PublicRouteDomain {
    return PublicRouteDomain(route.routeId, route.name, route.description, route.tagsList, route.imageList, route.isPublic)
}