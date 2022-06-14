package com.example.trampled_trails.domain.usecase.public

import com.example.trampled_trails.domain.entity.RouteDomain

interface UploadRouteToFirebaseUseCase {
    suspend fun invoke(route: RouteDomain, currentUser: String)
}