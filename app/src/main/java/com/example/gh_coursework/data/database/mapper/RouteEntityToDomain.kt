package com.example.gh_coursework.data.database.mapper

import com.example.gh_coursework.data.database.response.RoutePreviewResponse
import com.example.gh_coursework.domain.entity.RouteDomain

fun mapRouteResponseListToDomain(routeResponse: RoutePreviewResponse): RouteDomain {
    with(routeResponse) {
        return RouteDomain(
            route.routeId,
            route.name,
            route.description,
            route.rating,
            coordinates.map { mapPointEntityToDomain(it) })
    }
}