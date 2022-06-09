package com.example.trampled_trails.domain.usecase.public

import com.example.trampled_trails.domain.entity.PublicPointDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class FetchRoutePointsFromRemoteUseCaseImpl(private val repository: TravelRepository) : FetchRoutePointsFromRemoteUseCase {
    override fun invoke(routeId: String): Flow<List<PublicPointDomain>> {
        return repository.fetchRoutePoints(routeId)
    }
}