package com.example.trampled_trails.domain.usecase.point_preview

import com.example.trampled_trails.domain.entity.PointDetailsDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class DeletePointUseCaseImpl(private val repository: TravelRepository) : DeletePointUseCase {
    override suspend fun invoke(point: PointDetailsDomain) = (Dispatchers.IO) {
        repository.deletePoint(point)
    }
}