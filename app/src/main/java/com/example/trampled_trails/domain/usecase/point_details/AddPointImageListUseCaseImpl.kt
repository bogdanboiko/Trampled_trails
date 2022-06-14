package com.example.trampled_trails.domain.usecase.point_details

import com.example.trampled_trails.domain.entity.PointImageDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class AddPointImageListUseCaseImpl(private val repository: TravelRepository) :
    AddPointImageListUseCase {
    override suspend fun invoke(images: List<PointImageDomain>) = (Dispatchers.IO) {
        repository.addPointImages(images)
    }
}