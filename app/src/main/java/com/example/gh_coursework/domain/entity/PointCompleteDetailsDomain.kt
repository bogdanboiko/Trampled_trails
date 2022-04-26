package com.example.gh_coursework.domain.entity

data class PointCompleteDetailsDomain(
    val pointId: String,
    val x: Double,
    val y: Double,
    val tagList: List<PointTagDomain>,
    val imageList: List<PointImageDomain>,
    val caption: String,
    val description: String
)