package com.example.gh_coursework.domain.usecase.point_preview

import com.example.gh_coursework.domain.entity.PointPreviewDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class AddPointPreviewWithDetailsUseCaseImpl(private val repository: TravelRepository) :
    AddPointPreviewWithDetailsUseCase {
    override suspend fun invoke(point: PointPreviewDomain) = (Dispatchers.IO) {
        repository.addPointCoordinatesWithDetails(point)
    }
}