package com.example.trampled_trails.domain.usecase.image

import com.example.trampled_trails.domain.entity.PointImageDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetPointImagesUseCaseImpl(private val repository: TravelRepository) : GetPointImagesUseCase {
    override fun invoke(pointId: String): Flow<List<PointImageDomain>> {
        return repository.getPointImages(pointId)
    }
}