package com.example.gh_coursework.data.remote.entity

data class PublicPointEntity(
    val caption: String,
    val description: String,
    val tagsList: List<String>,
    val imageList: List<String>,
    val x: Double,
    val y: Double,
    val routeId: String?,
    val userId: String,
    val isRoutePoint: Boolean,
    val position: Int
)