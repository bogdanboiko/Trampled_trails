package com.example.gh_coursework.ui.point_details.model

data class PointDetailsModel(
    val pointId: Long,
    val tagList: List<PointTagModel>,
    val caption: String,
    val description: String
)