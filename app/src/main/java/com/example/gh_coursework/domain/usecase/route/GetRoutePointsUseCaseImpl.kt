package com.example.gh_coursework.domain.usecase.route

import com.example.gh_coursework.domain.entity.RoutePointDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetRoutePointsUseCaseImpl(private val repository: TravelRepository) : GetRoutePointsUseCase {
    override fun invoke(routeId: Int): Flow<List<RoutePointDomain>> {
        return repository.getRoutePoints(routeId)
    }
}