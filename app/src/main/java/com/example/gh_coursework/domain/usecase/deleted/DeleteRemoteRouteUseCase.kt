package com.example.gh_coursework.domain.usecase.deleted

interface DeleteRemoteRouteUseCase {
    suspend fun invoke(routeId: String)
}