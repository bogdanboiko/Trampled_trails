package com.example.trampled_trails.ui.route_details.mapper

import com.example.trampled_trails.domain.entity.RouteTagsDomain
import com.example.trampled_trails.ui.route_details.model.RouteTagsModel

fun mapRouteTagsModelToDomain(tag: RouteTagsModel): RouteTagsDomain {
    return RouteTagsDomain(tag.routeId, tag.tagId)
}