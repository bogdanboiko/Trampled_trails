package com.example.trampled_trails.data.remote.mapper

import com.example.trampled_trails.data.remote.entity.PublicRouteResponseEntity
import com.example.trampled_trails.domain.entity.PublicRouteDomain

fun mapPublicRouteResponseToDomain(publicRouteResponseEntity: PublicRouteResponseEntity): PublicRouteDomain  {
    with(publicRouteResponseEntity) {
        return PublicRouteDomain(routeId, name, description, tagsList, imageList, userId, isPublic)
    }
}