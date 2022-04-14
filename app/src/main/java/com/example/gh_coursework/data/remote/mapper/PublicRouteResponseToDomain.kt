package com.example.gh_coursework.data.remote.mapper

import com.example.gh_coursework.data.remote.entity.PublicRouteResponseEntity
import com.example.gh_coursework.domain.entity.PublicRouteDomain

fun mapPublicRouteResponseToDomain(route: PublicRouteResponseEntity): PublicRouteDomain {
    return PublicRouteDomain(route.documentId, route.name, route.description, route.tagsList, route.imageList)
}