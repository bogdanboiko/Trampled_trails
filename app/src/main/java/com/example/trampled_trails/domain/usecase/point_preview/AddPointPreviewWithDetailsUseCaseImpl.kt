package com.example.trampled_trails.domain.usecase.point_preview

import com.example.trampled_trails.domain.entity.PointPreviewDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class AddPointPreviewWithDetailsUseCaseImpl(private val repository: TravelRepository) :
    AddPointPreviewWithDetailsUseCase {
    override suspend fun invoke(point: PointPreviewDomain) = (Dispatchers.IO) {
        repository.addPointPreviewWithDetails(point)
    }
}