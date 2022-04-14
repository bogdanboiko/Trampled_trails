package com.example.gh_coursework.data.remote.entity

data class PublicRoutePointResponseEntity(
    val pointDocumentId: String,
    val caption: String,
    val description: String,
    val imageList: List<String>,
    val x: Double,
    val y: Double,
    val isRoutePoint: Boolean
)
