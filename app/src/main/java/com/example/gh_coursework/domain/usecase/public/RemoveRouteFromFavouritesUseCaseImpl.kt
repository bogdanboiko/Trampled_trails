package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.repository.TravelRepository

class RemoveRouteFromFavouritesUseCaseImpl(private val repository: TravelRepository): RemoveRouteFromFavouritesUseCase {
    override suspend fun invoke(routeId: String, userId: String) {
        repository.removeRouteFromFavourites(routeId, userId)
    }
}