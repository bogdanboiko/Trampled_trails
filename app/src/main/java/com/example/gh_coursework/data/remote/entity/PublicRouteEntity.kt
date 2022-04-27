package com.example.gh_coursework.data.remote.entity

import com.google.firebase.firestore.FieldValue

data class PublicRouteEntity(
    val name: String?,
    val description: String?,
    val rating: Double,
    val tagsList: List<String>,
    val imageList: List<String>,
    val userId: String,
    val isPublic: Boolean
)
