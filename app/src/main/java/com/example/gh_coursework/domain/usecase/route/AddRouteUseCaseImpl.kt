package com.example.gh_coursework.domain.usecase.route

import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.repository.TravelRepository

class AddRouteUseCaseImpl(private val repository: TravelRepository): AddRouteUseCase {
    override suspend fun invoke(route: RouteDomain) {
        repository.addRoute(route)
    }
}