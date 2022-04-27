package com.example.gh_coursework.data.remote.entity

data class PublicRouteResponseEntity(
    val routeId: String,
    val name: String,
    val description: String,
    val tagsList: List<String>,
    val imageList: List<String>,
    val isPublic: Boolean
)