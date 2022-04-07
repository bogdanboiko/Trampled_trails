package com.example.gh_coursework.data.remote.entity

data class PublicRoutePointEntity(
    val caption: String,
    val description: String,
    val tagList: List<String>,
    val imageList: List<String>,
    val x: Double,
    val y: Double
)