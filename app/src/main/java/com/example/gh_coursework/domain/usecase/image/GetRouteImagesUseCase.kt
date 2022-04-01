package com.example.gh_coursework.domain.usecase.image

import com.example.gh_coursework.domain.entity.RouteImageDomain
import kotlinx.coroutines.flow.Flow

interface GetRouteImagesUseCase {
    fun invoke(routeId: Long): Flow<List<RouteImageDomain>>
}