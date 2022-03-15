package com.example.gh_coursework.domain.usecase.point_preview

import com.example.gh_coursework.domain.entity.PointPreviewDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetPointsPreviewUseCaseImpl(private val repository: TravelRepository) :
    GetPointsPreviewUseCase {
    override fun invoke(): Flow<List<PointPreviewDomain>> {
        return repository.getPointOfInterestPreview()
    }

}