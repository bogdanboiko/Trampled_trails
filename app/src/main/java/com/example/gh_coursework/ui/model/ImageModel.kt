package com.example.gh_coursework.ui.model

sealed class ImageModel(val image: String) {
    data class PointImageModel(val pointId: Long, val pointImage: String): ImageModel(pointImage)
    data class RouteImageModel(val routeId: Long, val routeImage: String): ImageModel(routeImage)
}