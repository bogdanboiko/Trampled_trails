package com.example.gh_coursework.domain.usecase.public

interface ChangeRouteAccessUseCase {
    suspend fun invoke(routeId: String, isPublic: Boolean)
}