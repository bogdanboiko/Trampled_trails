package com.example.trampled_trails.domain.usecase.public

import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class AddRouteToFavouritesUseCaseImpl(private val repository: TravelRepository): AddRouteToFavouritesUseCase {
    override suspend fun invoke(routeId: String, userId: String) = (Dispatchers.IO) {
        repository.addRouteToFavourites(routeId, userId)
    }
}