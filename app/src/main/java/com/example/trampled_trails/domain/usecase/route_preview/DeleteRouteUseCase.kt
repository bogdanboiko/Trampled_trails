package com.example.trampled_trails.domain.usecase.route_preview

import com.example.trampled_trails.domain.entity.RouteDomain

interface DeleteRouteUseCase {
    suspend fun invoke(route: RouteDomain)
}