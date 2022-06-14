package com.example.trampled_trails.domain.usecase.point_tag

import com.example.trampled_trails.domain.entity.PointTagDomain
import kotlinx.coroutines.flow.Flow

interface GetPointTagListUseCase {
    fun invoke(): Flow<List<PointTagDomain>>
}