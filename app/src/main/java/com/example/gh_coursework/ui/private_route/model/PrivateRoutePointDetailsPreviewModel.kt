package com.example.gh_coursework.ui.private_route.model

import com.example.gh_coursework.ui.point_details.model.PointTagModel

data class PrivateRoutePointDetailsPreviewModel(
    val tagList: List<PointTagModel>,
    val caption: String,
    val description: String
)