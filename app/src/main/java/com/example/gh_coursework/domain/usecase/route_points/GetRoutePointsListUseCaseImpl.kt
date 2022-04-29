package com.example.gh_coursework.domain.usecase.route_points

import com.example.gh_coursework.domain.entity.PointDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetRoutePointsListUseCaseImpl(private val repository: TravelRepository): GetRoutePointsListUseCase {
    override fun invoke(routeId: String): Flow<List<PointDomain>> {
        return repository.getRoutePointsList(routeId)
    }
}