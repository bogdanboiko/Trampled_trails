package com.example.trampled_trails.domain.usecase.point_preview

import com.example.trampled_trails.domain.entity.PointDetailsDomain

interface DeletePointUseCase {
    suspend fun invoke(point: PointDetailsDomain)
}