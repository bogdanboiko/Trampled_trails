package com.example.trampled_trails.data.database.mapper.route_tag

import com.example.trampled_trails.data.database.entity.RouteTagsEntity
import com.example.trampled_trails.domain.entity.RouteTagsDomain

fun mapRouteTagsEntityToDomain(tag: RouteTagsEntity): RouteTagsDomain {
    return RouteTagsDomain(tag.routeId, tag.tagId)
}