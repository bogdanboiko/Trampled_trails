package com.example.trampled_trails.data.remote.mapper

import com.example.trampled_trails.data.remote.entity.PublicRouteEntity
import com.example.trampled_trails.domain.entity.RouteDomain

fun mapRouteDomainToPublicRouteEntity(route: RouteDomain, imageList: List<String>, uid: String): PublicRouteEntity {
    return PublicRouteEntity(
        route.name,
        route.description,
        route.tagsList.map { it.name },
        imageList,
        uid,
        route.isPublic
    )
}