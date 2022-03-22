package com.example.gh_coursework.data.database.mapper.route_tag

import com.example.gh_coursework.data.database.entity.RouteTagsEntity
import com.example.gh_coursework.domain.entity.RouteTagsDomain

fun mapRouteTagsDomainToEntity(tag: RouteTagsDomain): RouteTagsEntity {
    return RouteTagsEntity(tag.routeId, tag.tagId)
}