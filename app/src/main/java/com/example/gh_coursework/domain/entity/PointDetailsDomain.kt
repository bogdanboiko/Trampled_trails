package com.example.gh_coursework.domain.entity

data class PointDetailsDomain(
    val pointId: Long,
    val tagList: List<PointTagDomain>,
    val caption: String,
    val description: String
)
