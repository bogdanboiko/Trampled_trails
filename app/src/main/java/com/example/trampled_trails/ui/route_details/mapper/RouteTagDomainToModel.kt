package com.example.trampled_trails.ui.route_details.mapper

import com.example.trampled_trails.domain.entity.RouteTagDomain
import com.example.trampled_trails.ui.route_details.model.RouteTagModel

fun mapRouteTagDomainToModel(tag: RouteTagDomain): RouteTagModel {
    return RouteTagModel(tag.tagId, tag.name)
}