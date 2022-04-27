package com.example.gh_coursework.data.remote.entity

import com.google.firebase.firestore.FieldValue

data class PublicRoutePointEntity(
    val caption: String,
    val description: String,
    val imageList: List<String>,
    val x: Double,
    val y: Double,
    val isRoutePoint: Boolean,
    val position: Int
)