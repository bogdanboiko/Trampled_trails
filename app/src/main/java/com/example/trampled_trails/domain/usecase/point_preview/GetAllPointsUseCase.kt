package com.example.trampled_trails.domain.usecase.point_preview

import com.example.trampled_trails.domain.entity.PointDomain
import kotlinx.coroutines.flow.Flow

interface GetAllPointsUseCase {
    fun invoke(): Flow<List<PointDomain>>
}