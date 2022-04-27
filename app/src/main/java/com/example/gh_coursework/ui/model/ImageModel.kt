package com.example.gh_coursework.ui.model

sealed class ImageModel() {
    abstract val image: String
    abstract val isUploaded: Boolean

    data class PointImageModel(
        val pointId: String,
        override val image: String,
        override val isUploaded: Boolean
    ) : ImageModel()

    data class RouteImageModel(
        val routeId: String,
        override val image: String,
        override val isUploaded: Boolean
    ) : ImageModel()
}