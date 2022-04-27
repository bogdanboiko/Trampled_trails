package com.example.gh_coursework.domain.entity

data class PublicRouteDomain(
    val routeId: String,
    val name: String,
    val description: String,
    val tagsList: List<String>,
    val imageList: List<String>,
    val isPublic: Boolean
)