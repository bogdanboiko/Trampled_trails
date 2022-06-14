package com.example.trampled_trails.data.database.mapper.route_tag

import com.example.trampled_trails.data.database.entity.RouteTagEntity
import com.example.trampled_trails.domain.entity.RouteTagDomain

fun mapRouteTagEntityToDomain(tag: RouteTagEntity): RouteTagDomain {
    return RouteTagDomain(tag.tagId, tag.name)
}