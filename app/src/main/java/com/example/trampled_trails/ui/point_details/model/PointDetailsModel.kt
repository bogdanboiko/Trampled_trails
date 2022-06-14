package com.example.trampled_trails.ui.point_details.model

import com.example.trampled_trails.ui.private_image_details.model.ImageModel.PointImageModel

data class PointDetailsModel(
    val pointId: String,
    val tagList: List<PointTagModel> = emptyList(),
    val imageList: List<PointImageModel> = emptyList(),
    val caption: String,
    val description: String
)
