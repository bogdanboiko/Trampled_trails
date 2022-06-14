package com.example.trampled_trails.domain.usecase.route_details

import com.example.trampled_trails.domain.entity.RouteDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class UpdateRouteDetailsUseCaseImpl(private val repository: TravelRepository) :
    UpdateRouteDetailsUseCase {
    override suspend fun invoke(route: RouteDomain) = (Dispatchers.IO) {
        repository.updateRoute(route)
    }
}