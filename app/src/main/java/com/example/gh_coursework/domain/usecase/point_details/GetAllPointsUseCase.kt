package com.example.gh_coursework.domain.usecase.point_details

import com.example.gh_coursework.domain.entity.PointDomain
import kotlinx.coroutines.flow.Flow

interface GetAllPointsUseCase {
    fun invoke(): Flow<List<PointDomain>>
}