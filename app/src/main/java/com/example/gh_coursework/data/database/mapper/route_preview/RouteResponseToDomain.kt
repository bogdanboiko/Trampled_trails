package com.example.gh_coursework.data.database.mapper.route_preview

import com.example.gh_coursework.data.database.mapper.images.mapRouteImageEntityToDomain
import com.example.gh_coursework.data.database.mapper.route_tag.mapRouteTagEntityToDomain
import com.example.gh_coursework.data.database.response.RoutePreviewResponse
import com.example.gh_coursework.domain.entity.RouteDomain

fun mapRouteResponseToDomain(routeResponse: RoutePreviewResponse): RouteDomain {
    with(routeResponse) {
        return RouteDomain(
            route.routeId,
            route.name,
            route.description,
            route.rating,
            tagList.map(::mapRouteTagEntityToDomain),
            imageList.map(::mapRouteImageEntityToDomain)
        )
    }
}