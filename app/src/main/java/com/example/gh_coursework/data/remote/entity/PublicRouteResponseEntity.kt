package com.example.gh_coursework.data.remote.entity

data class PublicRouteResponseEntity(
    val documentId: String,
    val name: String,
    val description: String,
    val tagsList: List<String>,
    val imageList: List<String>
)
