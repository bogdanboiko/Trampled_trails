package com.example.gh_coursework.ui.point_details.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PointTagModel(
    val tagId: Long?,
    val name: String
): Parcelable