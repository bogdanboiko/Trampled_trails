package com.example.gh_coursework.domain.usecase.deleted

import com.example.gh_coursework.domain.repository.TravelRepository

class DeleteRemoteRouteUseCaseImpl(private val repository: TravelRepository): DeleteRemoteRouteUseCase {
    override suspend fun invoke(routeId: String) {
        repository.deleteRemoteRoute(routeId)
    }
}