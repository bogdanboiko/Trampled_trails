package com.example.gh_coursework.domain.entity

data class RouteDetailsDomain(
    var routeId: Long?,
    val name: String?,
    val description: String?,
    val rating: Double,
    val tagsList: List<RouteTagDomain>,
    val imageList: List<RouteImageDomain>?
)


