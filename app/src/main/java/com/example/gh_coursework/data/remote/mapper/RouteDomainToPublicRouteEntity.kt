package com.example.gh_coursework.data.remote.mapper

import android.net.Uri
import com.example.gh_coursework.data.remote.entity.PublicRouteEntity
import com.example.gh_coursework.domain.entity.RouteDomain

fun mapRouteDomainToPublicRouteEntity(route: RouteDomain, imageList: List<String>): PublicRouteEntity {
    return PublicRouteEntity(
        route.name,
        route.description,
        0.0,
        route.tagsList.map { it.name },
        imageList
    )
}