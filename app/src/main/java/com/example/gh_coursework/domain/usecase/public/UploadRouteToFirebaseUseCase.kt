package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.entity.RouteDomain

interface UploadRouteToFirebaseUseCase {
    suspend fun invoke(route: RouteDomain, currentUser: String)
}