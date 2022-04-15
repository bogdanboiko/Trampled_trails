package com.example.gh_coursework.data.remote.entity

data class PublicRouteEntity(
    val name: String?,
    val description: String?,
    val rating: Double,
    val tagsList: List<String>,
    val imageList: List<String>
)
