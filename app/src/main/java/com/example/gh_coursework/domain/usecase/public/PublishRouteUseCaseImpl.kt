package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.entity.RoutePointDomain
import com.example.gh_coursework.domain.repository.TravelRepository

class PublishRouteUseCaseImpl(private val repository: TravelRepository): PublishRouteUseCase {
    override suspend fun invoke(
        route: RouteDomain,
        routePoints: List<RoutePointDomain>,
        currentUser: String
    ) {
        repository.publishRoute(route, routePoints, currentUser)
    }
}