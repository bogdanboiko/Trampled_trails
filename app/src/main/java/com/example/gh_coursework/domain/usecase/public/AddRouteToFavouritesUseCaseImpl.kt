package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.repository.TravelRepository

class AddRouteToFavouritesUseCaseImpl(private val repository: TravelRepository): AddRouteToFavouritesUseCase {
    override suspend fun invoke(routeId: String, userId: String) {
        repository.addRouteToFavourites(routeId, userId)
    }
}