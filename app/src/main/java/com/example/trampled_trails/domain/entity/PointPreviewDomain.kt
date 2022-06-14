package com.example.trampled_trails.domain.entity

data class PointPreviewDomain(
    val pointId: String,
    val x: Double,
    val y: Double,
    val routeId: String?,
    val isRoutePoint: Boolean,
    val position: Long?
)
