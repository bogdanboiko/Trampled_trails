package com.example.gh_coursework.domain.usecase.point_details

import com.example.gh_coursework.domain.entity.PointCompleteDetailsDomain
import kotlinx.coroutines.flow.Flow

interface GetAllPointsDetails {
    fun invoke(): Flow<List<PointCompleteDetailsDomain>>
}