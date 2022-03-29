package com.example.gh_coursework.data.database.mapper.route_preview

import com.example.gh_coursework.data.database.mapper.images.mapRouteImageEntityToDomain
import com.example.gh_coursework.data.database.mapper.point_details.mapPointDetailsEntityToDomain
import com.example.gh_coursework.data.database.mapper.point_preview.mapPointEntityToDomain
import com.example.gh_coursework.data.database.mapper.route_tag.mapRouteTagEntityToDomain
import com.example.gh_coursework.data.database.response.RoutePreviewResponse
import com.example.gh_coursework.domain.entity.RouteDomain

fun mapRouteResponseListToDomain(routeResponse: RoutePreviewResponse): RouteDomain {
    with(routeResponse) {
        return RouteDomain(
            route.routeId,
            route.name,
            route.description,
            route.rating,
            points.map(::mapPointDetailsEntityToDomain),
            details.tagList.map(::mapRouteTagEntityToDomain),
            details.imageList.map(::mapRouteImageEntityToDomain),
            coordinates.map(::mapPointEntityToDomain)
        )
    }
}