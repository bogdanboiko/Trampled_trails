package com.example.gh_coursework.domain.usecase.route_points

import com.example.gh_coursework.domain.entity.RoutePointDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetRoutePointsListUseCaseImpl(private val repository: TravelRepository): GetRoutePointsListUseCase {
    override fun invoke(routeId: Long): Flow<List<RoutePointDomain>> {
        return repository.getRoutePointsList(routeId)
    }
}