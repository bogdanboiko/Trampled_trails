package com.example.trampled_trails.domain.usecase.image

import com.example.trampled_trails.domain.entity.RouteImageDomain

interface AddRouteImageListUseCase {
    suspend fun invoke(images: List<RouteImageDomain>)
}