package com.example.trampled_trails.domain.usecase.deleted

interface DeleteRemoteRouteUseCase {
    suspend fun invoke(routeId: String)
}