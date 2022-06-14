package com.example.trampled_trails.domain.usecase.public

interface AddRouteToFavouritesUseCase {
    suspend fun invoke(routeId: String, userId: String)
}