package com.example.trampled_trails.domain.usecase.point_details

import com.example.trampled_trails.domain.entity.PointDetailsDomain

interface UpdatePointDetailsUseCase {
    suspend fun invoke(details: PointDetailsDomain)
}