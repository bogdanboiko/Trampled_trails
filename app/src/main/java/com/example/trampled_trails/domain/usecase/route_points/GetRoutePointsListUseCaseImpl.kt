package com.example.trampled_trails.domain.usecase.route_points

import com.example.trampled_trails.domain.entity.PointDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetRoutePointsListUseCaseImpl(private val repository: TravelRepository): GetRoutePointsListUseCase {
    override fun invoke(routeId: String): Flow<List<PointDomain>> {
        return repository.getRoutePointsList(routeId)
    }
}