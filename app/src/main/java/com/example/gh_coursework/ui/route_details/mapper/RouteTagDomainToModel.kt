package com.example.gh_coursework.ui.route_details.mapper

import com.example.gh_coursework.domain.entity.RouteTagDomain
import com.example.gh_coursework.ui.route_details.model.RouteTagModel

fun mapRouteTagDomainToModel(tag: RouteTagDomain): RouteTagModel {
    return RouteTagModel(tag.tagId, tag.name)
}