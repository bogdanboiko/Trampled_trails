package com.example.gh_coursework.domain.usecase.public

interface MakePrivateRoutePublicUseCase {
    suspend fun invoke(routeId: String)
}