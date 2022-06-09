package com.example.trampled_trails.domain.usecase.image

import com.example.trampled_trails.domain.entity.PointImageDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class DeletePointImageUseCaseImpl(private val repository: TravelRepository) : DeletePointImageUseCase {
    override suspend fun invoke(image: PointImageDomain) = (Dispatchers.IO) {
        repository.deletePointImage(image)
    }
}