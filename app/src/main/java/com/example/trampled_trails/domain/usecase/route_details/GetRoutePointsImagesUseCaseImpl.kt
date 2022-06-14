package com.example.trampled_trails.domain.usecase.route_details

import com.example.trampled_trails.domain.entity.RoutePointsImagesDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetRoutePointsImagesUseCaseImpl(private val repository: TravelRepository) :
    GetRoutePointsImagesUseCase {
    override fun invoke(routeId: String): Flow<List<RoutePointsImagesDomain>> {
        return repository.getRoutePointsImagesList(routeId)
    }
}