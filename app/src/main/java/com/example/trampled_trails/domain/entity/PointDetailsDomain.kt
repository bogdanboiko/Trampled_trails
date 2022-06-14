package com.example.trampled_trails.domain.entity

data class PointDetailsDomain(
    val pointId: String,
    val tagList: List<PointTagDomain>,
    val imageList: List<PointImageDomain>,
    val caption: String,
    val description: String
)
