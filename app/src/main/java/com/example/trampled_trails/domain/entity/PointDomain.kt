package com.example.trampled_trails.domain.entity

data class PointDomain(
    val pointId: String,
    val caption: String,
    val description: String,
    val tagsList: List<PointTagDomain>,
    val imageList: List<PointImageDomain>,
    val x: Double,
    val y: Double,
    val routeId: String?,
    val isRoutePoint: Boolean,
    val position: Long?
)