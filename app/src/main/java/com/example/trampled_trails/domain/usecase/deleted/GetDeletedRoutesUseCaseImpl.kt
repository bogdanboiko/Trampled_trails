package com.example.trampled_trails.domain.usecase.deleted

import com.example.trampled_trails.domain.entity.DeletedRouteDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetDeletedRoutesUseCaseImpl(private val repository: TravelRepository): GetDeletedRoutesUseCase {
    override fun invoke(): Flow<List<DeletedRouteDomain>> {
        return repository.getDeletedRoutes()
    }
}