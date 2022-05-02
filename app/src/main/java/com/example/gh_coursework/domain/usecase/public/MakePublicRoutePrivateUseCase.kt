package com.example.gh_coursework.domain.usecase.public

interface MakePublicRoutePrivateUseCase {
    suspend fun invoke(routeId: String)
}