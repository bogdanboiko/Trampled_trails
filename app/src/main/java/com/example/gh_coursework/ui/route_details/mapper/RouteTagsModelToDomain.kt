package com.example.gh_coursework.ui.route_details.mapper

import com.example.gh_coursework.domain.entity.RouteTagsDomain
import com.example.gh_coursework.ui.route_details.model.RouteTagsModel

fun mapRouteTagsModelToDomain(tag: RouteTagsModel): RouteTagsDomain {
    return RouteTagsDomain(tag.routeId, tag.tagId)
}