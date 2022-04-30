package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.entity.PublicPointDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class FetchRoutePointsFromRemoteUseCaseImpl(private val repository: TravelRepository) : FetchRoutePointsFromRemoteUseCase {
    override fun invoke(routeId: String): Flow<List<PublicPointDomain>> {
        return repository.fetchRoutePoints(routeId)
    }
}