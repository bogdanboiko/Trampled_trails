package com.example.trampled_trails.domain.entity

data class RouteDomain(
    val routeId: String,
    val name: String?,
    val description: String?,
    val tagsList: List<RouteTagDomain>,
    val imageList: List<RouteImageDomain>,
    val isPublic: Boolean
)
