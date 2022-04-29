package com.example.gh_coursework.domain.usecase.point_preview

import com.example.gh_coursework.domain.entity.PointPreviewDomain
import com.example.gh_coursework.domain.repository.TravelRepository

class AddPointPreviewWithDetailsUseCaseImpl(private val repository: TravelRepository) :
    AddPointPreviewWithDetailsUseCase {
    override suspend fun invoke(point: PointPreviewDomain) {
        repository.addPointCoordinatesWithDetails(point)
    }
}