package com.example.trampled_trails.ui.point_details.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PointTagModel(
    val tagId: Long?,
    val name: String
): Parcelable