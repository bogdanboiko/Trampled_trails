package com.example.gh_coursework.domain.usecase.point_preview

import com.example.gh_coursework.domain.entity.PointPreviewDomain
import com.example.gh_coursework.domain.repository.TravelRepository

class AddPointPreviewUseCaseImpl(private val repository: TravelRepository) :
    AddPointPreviewUseCase {
    override suspend fun invoke(point: PointPreviewDomain) {
        repository.addPointOfInterestCoordinates(point)
    }
}