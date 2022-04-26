package com.example.gh_coursework.ui.model

sealed class ImageModel(val image: String) {
    data class PointImageModel(val pointId: String, val pointImage: String): ImageModel(pointImage)
    data class RouteImageModel(val routeId: String, val routeImage: String): ImageModel(routeImage)
}