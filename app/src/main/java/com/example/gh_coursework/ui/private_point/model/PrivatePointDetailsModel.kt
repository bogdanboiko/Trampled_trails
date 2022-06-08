package com.example.gh_coursework.ui.private_point.model

import com.example.gh_coursework.ui.point_details.model.PointTagModel
import com.example.gh_coursework.ui.private_image_details.model.ImageModel.PointImageModel

data class PrivatePointDetailsModel(
    val pointId: String,
    val x: Double,
    val y: Double,
    val imageList: List<PointImageModel> = emptyList(),
    val tagList: List<PointTagModel> = emptyList(),
    val caption: String = "",
    val description: String = "",
    val routeId: String? = ""
)