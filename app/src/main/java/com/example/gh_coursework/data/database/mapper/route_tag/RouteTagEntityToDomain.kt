package com.example.gh_coursework.data.database.mapper.route_tag

import com.example.gh_coursework.data.database.entity.RouteTagEntity
import com.example.gh_coursework.domain.entity.RouteTagDomain

fun mapRouteTagEntityToDomain(tag: RouteTagEntity): RouteTagDomain {
    return RouteTagDomain(tag.tagId, tag.name)
}