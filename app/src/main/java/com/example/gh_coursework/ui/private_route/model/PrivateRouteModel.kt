package com.example.gh_coursework.ui.private_route.model

data class PrivateRouteModel(
    val routeId: Long?,
    val name: String?,
    val description: String?,
    val rating: Double?,
    val coordinatesList: List<PrivateRoutePointModel>,
    var imgResources: String?
)