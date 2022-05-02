package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.repository.TravelRepository

class ChangeRouteAccessUseCaseImpl(private val repository: TravelRepository): ChangeRouteAccessUseCase {
    override suspend fun invoke(routeId: String, isPublic: Boolean) {
        repository.changeRouteAccess(routeId, isPublic)
    }
}