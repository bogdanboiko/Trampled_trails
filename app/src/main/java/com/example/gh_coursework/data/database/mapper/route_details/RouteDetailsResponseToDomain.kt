package com.example.gh_coursework.data.database.mapper.route_details

import com.example.gh_coursework.data.database.mapper.route_tag.mapRouteTagEntityToDomain
import com.example.gh_coursework.data.database.response.RouteDetailsResponse
import com.example.gh_coursework.domain.entity.RouteDetailsDomain

fun mapRouteDetailsResponseToDomain(route: RouteDetailsResponse): RouteDetailsDomain {
    return RouteDetailsDomain(
        route.routeDetails.routeId,
        route.routeDetails.name,
        route.routeDetails.description,
        route.routeDetails.rating,
        route.tagList.map(::mapRouteTagEntityToDomain)
    )
}