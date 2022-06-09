package com.example.trampled_trails.domain.usecase.point_details

import com.example.trampled_trails.domain.entity.PointDetailsDomain
import kotlinx.coroutines.flow.Flow

interface GetPointDetailsUseCase {
    fun invoke(pointId: String): Flow<PointDetailsDomain?>
}