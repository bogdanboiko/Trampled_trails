package com.example.gh_coursework.domain.usecase.route_preview

import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.entity.RoutePointDomain
import com.example.gh_coursework.domain.repository.TravelRepository

class AddRouteUseCaseImpl(private val repository: TravelRepository): AddRouteUseCase {
    override suspend fun invoke(route: RouteDomain, pointsList: List<RoutePointDomain>) {
        repository.addRoute(route, pointsList)
    }
}