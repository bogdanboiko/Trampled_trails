package com.example.gh_coursework.domain.usecase.route_details

import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.repository.TravelRepository

class UpdateRouteDetailsUseCaseImpl(private val repository: TravelRepository) :
    UpdateRouteDetailsUseCase {
    override suspend fun invoke(route: RouteDomain) {
        repository.updateRoute(route)
    }
}