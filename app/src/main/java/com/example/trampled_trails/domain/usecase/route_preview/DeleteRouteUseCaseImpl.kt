package com.example.trampled_trails.domain.usecase.route_preview

import com.example.trampled_trails.domain.entity.RouteDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class DeleteRouteUseCaseImpl(private val repository: TravelRepository) : DeleteRouteUseCase {
    override suspend fun invoke(route: RouteDomain) = (Dispatchers.IO) {
        repository.deleteRoute(route)
    }
}