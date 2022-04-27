package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.entity.PublicRoutePointDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class FetchRoutePointsFromRemoteUseCaseImpl(private val repository: TravelRepository) : FetchRoutePointsFromRemoteUseCase {
    override fun invoke(routeId: String): Flow<List<PublicRoutePointDomain>> {
        return repository.fetchRoutePoints(routeId)
    }
}