package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class RemoveRouteFromFavouritesUseCaseImpl(private val repository: TravelRepository): RemoveRouteFromFavouritesUseCase {
    override suspend fun invoke(routeId: String, userId: String) = (Dispatchers.IO) {
        repository.removeRouteFromFavourites(routeId, userId)
    }
}