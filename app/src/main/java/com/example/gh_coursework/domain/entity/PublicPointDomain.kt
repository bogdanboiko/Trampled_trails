package com.example.gh_coursework.domain.entity

data class PublicPointDomain(
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