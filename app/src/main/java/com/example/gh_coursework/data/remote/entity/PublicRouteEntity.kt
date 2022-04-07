package com.example.gh_coursework.data.remote.entity

import com.example.gh_coursework.domain.entity.RouteImageDomain

data class PublicRouteEntity(
    val name: String?,
    val description: String?,
    val tagsList: List<String>,
    val imageList: List<String>
)
