package com.example.gh_coursework.data.remote.mapper

import com.example.gh_coursework.data.remote.entity.PublicRouteResponseEntity
import com.example.gh_coursework.domain.entity.PublicRouteDomain

fun mapPublicRouteResponseToDomain(publicRouteResponseEntity: PublicRouteResponseEntity): PublicRouteDomain  {
    with(publicRouteResponseEntity) {
        return PublicRouteDomain(routeId, name, description, tagsList, imageList, isPublic)
    }
}