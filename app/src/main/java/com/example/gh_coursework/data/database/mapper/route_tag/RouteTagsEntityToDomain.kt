package com.example.gh_coursework.data.database.mapper.route_tag

import com.example.gh_coursework.data.database.entity.RouteTagsEntity
import com.example.gh_coursework.domain.entity.RouteTagsDomain

fun mapRouteTagsEntityToDomain(tag: RouteTagsEntity): RouteTagsDomain {
    return RouteTagsDomain(tag.routeId, tag.tagId)
}