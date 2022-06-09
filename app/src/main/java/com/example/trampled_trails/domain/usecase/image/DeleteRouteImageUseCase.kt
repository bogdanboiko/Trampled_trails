package com.example.trampled_trails.domain.usecase.image

import com.example.trampled_trails.domain.entity.RouteImageDomain

interface DeleteRouteImageUseCase {
    suspend fun invoke(image: RouteImageDomain)
}