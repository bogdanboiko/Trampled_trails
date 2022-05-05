package com.example.gh_coursework.domain.usecase.point_preview

import com.example.gh_coursework.domain.entity.PointDomain
import kotlinx.coroutines.flow.Flow

interface GetAllPointsDetailsUseCase {
    fun invoke(): Flow<List<PointDomain>>
}