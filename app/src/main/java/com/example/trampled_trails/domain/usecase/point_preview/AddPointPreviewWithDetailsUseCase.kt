package com.example.trampled_trails.domain.usecase.point_preview

import com.example.trampled_trails.domain.entity.PointPreviewDomain

interface AddPointPreviewWithDetailsUseCase {
    suspend fun invoke(point: PointPreviewDomain)
}