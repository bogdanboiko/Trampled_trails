package com.example.gh_coursework.domain.usecase.image

import com.example.gh_coursework.domain.entity.PointImageDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetPointImagesUseCaseImpl(private val repository: TravelRepository) : GetPointImagesUseCase {
    override fun invoke(pointId: String): Flow<List<PointImageDomain>> {
        return repository.getPointImages(pointId)
    }
}