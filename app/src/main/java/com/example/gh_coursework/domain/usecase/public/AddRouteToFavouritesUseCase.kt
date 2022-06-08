package com.example.gh_coursework.domain.usecase.public

interface AddRouteToFavouritesUseCase {
    suspend fun invoke(routeId: String, userId: String)
}