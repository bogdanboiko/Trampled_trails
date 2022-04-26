package com.example.gh_coursework.domain.entity

data class RoutePointDomain(
    val pointId: String,
    val caption: String,
    val description: String,
    val tagList: List<PointTagDomain>,
    val imageList: List<PointImageDomain>,
    val x: Double,
    val y: Double,
    val isRoutePoint: Boolean
)