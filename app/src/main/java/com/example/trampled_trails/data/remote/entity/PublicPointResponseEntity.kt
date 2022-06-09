package com.example.trampled_trails.data.remote.entity

data class PublicPointResponseEntity(
    val pointId: String,
    val caption: String,
    val description: String,
    val tagsList: List<String>,
    val imageList: List<String>,
    val x: Double,
    val y: Double,
    val routeId: String?,
    val isRoutePoint: Boolean,
    val position: Long
)
