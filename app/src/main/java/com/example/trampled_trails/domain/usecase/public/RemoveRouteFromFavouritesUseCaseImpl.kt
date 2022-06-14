package com.example.trampled_trails.domain.usecase.public

import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class RemoveRouteFromFavouritesUseCaseImpl(private val repository: TravelRepository): RemoveRouteFromFavouritesUseCase {
    override suspend fun invoke(routeId: String, userId: String) = (Dispatchers.IO) {
        repository.removeRouteFromFavourites(routeId, userId)
    }
}