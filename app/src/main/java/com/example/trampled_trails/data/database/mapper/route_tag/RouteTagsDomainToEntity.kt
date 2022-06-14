package com.example.trampled_trails.data.database.mapper.route_tag

import com.example.trampled_trails.data.database.entity.RouteTagsEntity
import com.example.trampled_trails.domain.entity.RouteTagsDomain

fun mapRouteTagsDomainToEntity(tag: RouteTagsDomain): RouteTagsEntity {
    return RouteTagsEntity(tag.routeId, tag.tagId)
}