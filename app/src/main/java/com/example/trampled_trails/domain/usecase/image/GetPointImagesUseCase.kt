package com.example.trampled_trails.domain.usecase.image

import com.example.trampled_trails.domain.entity.PointImageDomain
import kotlinx.coroutines.flow.Flow

interface GetPointImagesUseCase {
    fun invoke(pointId: String): Flow<List<PointImageDomain>>
}