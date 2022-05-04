package com.example.gh_coursework.ui.private_point.model

import com.example.gh_coursework.ui.point_details.model.PointTagModel
import com.example.gh_coursework.ui.private_image_details.model.ImageModel.PointImageModel

data class PrivatePointDetailsModel(
    val pointId: String,
    val x: Double,
    val y: Double,
    val imageList: List<PointImageModel>,
    val tagList: List<PointTagModel>,
    val caption: String,
    val description: String,
    val routeId: String?
)