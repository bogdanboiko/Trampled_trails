package com.example.trampled_trails.domain.usecase.public

interface RemoveRouteFromFavouritesUseCase {
    suspend fun invoke(routeId: String, userId: String)
}