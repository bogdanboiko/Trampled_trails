package com.example.gh_coursework.domain.entity

class PublicRoutePointDomain(
    val pointId: String,
    val caption: String,
    val description: String,
    val imageList: List<String>,
    val x: Double,
    val y: Double,
    val isRoutePoint: Boolean
)