package com.example.trampled_trails.ui.public_route.model

data class PublicRouteModel(
    val routeId: String,
    val name: String,
    val description: String,
    val tagsList: List<String>,
    val imageList: List<String>,
    val userId: String,
    val isPublic: Boolean
)