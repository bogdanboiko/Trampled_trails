package com.example.gh_coursework.domain.usecase.route

import com.example.gh_coursework.domain.repository.TravelRepository

class DeleteRouteUseCaseImpl(private val repository: TravelRepository) : DeleteRouteUseCase {
    override suspend fun invoke(routeId: Int) {
        repository.deleteRoute(routeId)
        repository.deleteRoutePoints(routeId)
    }
}