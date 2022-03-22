package com.example.gh_coursework.domain.usecase.route_preview

import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.repository.TravelRepository

class DeleteRouteUseCaseImpl(private val repository: TravelRepository) : DeleteRouteUseCase {
    override suspend fun invoke(route: RouteDomain) {
        repository.deleteRoute(route)
    }
}