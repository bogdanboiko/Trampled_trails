package com.example.trampled_trails.domain.usecase.main_activity

import com.example.trampled_trails.domain.entity.PointDomain
import kotlinx.coroutines.flow.Flow

interface GetAllPointsUseCase {
    fun invoke(): Flow<List<PointDomain>>
}