package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.entity.PublicRoutePointDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class FetchRoutePointsUseCaseImpl(private val repository: TravelRepository) : FetchRoutePointsUseCase {
    override fun invoke(routeId: String): Flow<List<PublicRoutePointDomain>> {
        return repository.fetchRoutePoints(routeId)
    }
}