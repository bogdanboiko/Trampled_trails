package com.example.trampled_trails.ui.route_details.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RouteTagModel(
    val tagId: Long,
    val name: String
): Parcelable
