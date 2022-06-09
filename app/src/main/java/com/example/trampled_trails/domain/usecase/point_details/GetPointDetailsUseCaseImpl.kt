package com.example.trampled_trails.domain.usecase.point_details

import com.example.trampled_trails.domain.entity.PointDetailsDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetPointDetailsUseCaseImpl(private val repository: TravelRepository) :
    GetPointDetailsUseCase {
    override fun invoke(pointId: String): Flow<PointDetailsDomain?> {
       return repository.getPointDetails(pointId)
    }
}