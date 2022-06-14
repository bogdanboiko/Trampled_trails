package com.example.trampled_trails.data.remote.entity

data class PublicRouteResponseEntity(
    val routeId: String,
    val name: String,
    val description: String,
    val tagsList: List<String>,
    val imageList: List<String>,
    val userId: String,
    val isPublic: Boolean
)