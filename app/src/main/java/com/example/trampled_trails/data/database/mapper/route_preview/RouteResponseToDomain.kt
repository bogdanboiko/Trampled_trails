package com.example.trampled_trails.data.database.mapper.route_preview

import com.example.trampled_trails.data.database.mapper.images.mapRouteImageEntityToDomain
import com.example.trampled_trails.data.database.mapper.route_tag.mapRouteTagEntityToDomain
import com.example.trampled_trails.data.database.response.RoutePreviewResponse
import com.example.trampled_trails.domain.entity.RouteDomain

fun mapRouteResponseToDomain(routeResponse: RoutePreviewResponse): RouteDomain {
    with(routeResponse) {
        return RouteDomain(
            route.routeId,
            route.name,
            route.description,
            tagList.map(::mapRouteTagEntityToDomain),
            imageList.map(::mapRouteImageEntityToDomain),
            route.isPublic
        )
    }
}