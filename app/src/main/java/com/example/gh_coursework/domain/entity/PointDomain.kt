package com.example.gh_coursework.domain.entity

data class PointDomain(
    val pointId: String,
    val caption: String,
    val description: String,
    val tagsList: List<PointTagDomain>,
    val imageList: List<PointImageDomain>,
    val x: Double,
    val y: Double,
    val routeId: String?,
    val isRoutePoint: Boolean
)