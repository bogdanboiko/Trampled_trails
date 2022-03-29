package com.example.gh_coursework.domain.entity

data class RouteDomain(
    val routeId: Long?,
    val name: String?,
    val description: String?,
    val rating: Double?,
    val points: List<PointDetailsDomain?>,
    val tagsList: List<RouteTagDomain>,
    val imageList: List<RouteImageDomain>,
    val coordinates: List<PointPreviewDomain>
)
