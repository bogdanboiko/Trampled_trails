package com.example.trampled_trails.domain.usecase.point_details

import com.example.trampled_trails.domain.entity.PointDetailsDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class UpdatePointDetailsUseCaseImpl(private val repository: TravelRepository) :
    UpdatePointDetailsUseCase {
    override suspend fun invoke(details: PointDetailsDomain) = (Dispatchers.IO) {
        repository.updatePointDetails(details)
    }
}