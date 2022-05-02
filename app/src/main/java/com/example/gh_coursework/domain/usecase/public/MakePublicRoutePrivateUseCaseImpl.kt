package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.repository.TravelRepository

class MakePublicRoutePrivateUseCaseImpl(private val repository: TravelRepository): MakePublicRoutePrivateUseCase {
    override suspend fun invoke(routeId: String) {
        repository.makePublicRoutePrivate(routeId)
    }
}