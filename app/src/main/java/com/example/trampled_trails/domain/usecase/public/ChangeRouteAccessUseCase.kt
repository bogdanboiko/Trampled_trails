package com.example.trampled_trails.domain.usecase.public

interface ChangeRouteAccessUseCase {
    suspend fun invoke(routeId: String, isPublic: Boolean)
}