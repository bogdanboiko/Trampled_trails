package com.example.gh_coursework.domain.entity

data class PublicRoutePointDomain(
    val pointId: String,
    val caption: String,
    val description: String,
    val imageList: List<String>,
    val x: Double,
    val y: Double,
    val isRoutePoint: Boolean
)