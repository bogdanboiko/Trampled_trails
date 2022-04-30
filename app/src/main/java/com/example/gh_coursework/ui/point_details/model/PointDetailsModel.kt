package com.example.gh_coursework.ui.point_details.model

import com.example.gh_coursework.ui.private_image_details.model.ImageModel.PointImageModel

data class PointDetailsModel(
    val pointId: String,
    val tagList: List<PointTagModel>,
    val imageList: List<PointImageModel>,
    val caption: String,
    val description: String
)
