package com.example.gh_coursework.ui.route_details.model

import com.example.gh_coursework.ui.model.ImageModel

data class RouteCompleteModel(
    val route: RouteDetailsModel,
    val pointsImagesList: List<RoutePointsImagesModel>
)