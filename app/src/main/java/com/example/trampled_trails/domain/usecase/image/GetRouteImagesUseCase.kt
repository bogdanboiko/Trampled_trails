package com.example.trampled_trails.domain.usecase.image

import com.example.trampled_trails.domain.entity.RouteImageDomain
import kotlinx.coroutines.flow.Flow

interface GetRouteImagesUseCase {
    fun invoke(routeId: String): Flow<List<RouteImageDomain>>
}