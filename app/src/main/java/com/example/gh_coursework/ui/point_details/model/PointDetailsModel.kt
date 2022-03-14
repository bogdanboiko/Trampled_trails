package com.example.gh_coursework.ui.point_details.model

data class PointDetailsModel(
    val pointId: Int,
    val tagList: List<PointTagModel>,
    val caption: String,
    val description: String
)
