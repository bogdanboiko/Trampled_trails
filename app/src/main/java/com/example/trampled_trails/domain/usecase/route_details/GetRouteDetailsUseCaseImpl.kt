package com.example.trampled_trails.domain.usecase.route_details

import com.example.trampled_trails.domain.entity.RouteDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetRouteDetailsUseCaseImpl(private val repository: TravelRepository): GetRouteDetailsUseCase {
    override fun invoke(routeId: String): Flow<RouteDomain> {
        return repository.getRouteDetails(routeId)
    }
}