package com.example.trampled_trails.ui.public_route.model

data class RoutePointModel(
    val pointId: String,
    val caption: String,
    val description: String,
    val imageList: List<String>,
    val x: Double,
    val y: Double,
    val isRoutePoint: Boolean
)