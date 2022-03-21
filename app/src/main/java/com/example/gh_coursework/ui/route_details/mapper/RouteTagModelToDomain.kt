package com.example.gh_coursework.ui.route_details.mapper

import com.example.gh_coursework.domain.entity.RouteTagDomain
import com.example.gh_coursework.ui.route_details.model.RouteTagModel

fun mapRouteTagModelToDomain(tag: RouteTagModel): RouteTagDomain {
    return RouteTagDomain(tag.tagId, tag.name)
}