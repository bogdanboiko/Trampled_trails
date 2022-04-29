package com.example.gh_coursework.data.remote.entity

data class PublicRoutePointEntity(
    val caption: String,
    val description: String,
    val imageList: List<String>,
    val x: Double,
    val y: Double,
    val routeId: String?,
    val isRoutePoint: Boolean,
    val position: Int
)