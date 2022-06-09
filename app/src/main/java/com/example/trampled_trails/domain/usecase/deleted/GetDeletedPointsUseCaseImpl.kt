package com.example.trampled_trails.domain.usecase.deleted

import com.example.trampled_trails.domain.entity.DeletedPointDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetDeletedPointsUseCaseImpl(private val repository: TravelRepository): GetDeletedPointsUseCase {
    override fun invoke(): Flow<List<DeletedPointDomain>> {
        return repository.getDeletedPoints()
    }
}