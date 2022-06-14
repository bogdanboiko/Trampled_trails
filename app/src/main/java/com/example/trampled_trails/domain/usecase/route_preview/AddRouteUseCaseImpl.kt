package com.example.trampled_trails.domain.usecase.route_preview

import com.example.trampled_trails.domain.entity.RouteDomain
import com.example.trampled_trails.domain.entity.PointPreviewDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class AddRouteUseCaseImpl(private val repository: TravelRepository): AddRouteUseCase {
    override suspend fun invoke(route: RouteDomain, pointsList: List<PointPreviewDomain>) = (Dispatchers.IO) {
        repository.addRoute(route, pointsList)
    }
}