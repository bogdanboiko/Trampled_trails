package com.example.gh_coursework.domain.usecase.public

interface RemoveRouteFromFavouritesUseCase {
    suspend fun invoke(routeId: String, userId: String)
}