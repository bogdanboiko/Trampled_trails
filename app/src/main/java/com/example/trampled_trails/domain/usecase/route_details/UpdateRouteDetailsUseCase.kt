package com.example.trampled_trails.domain.usecase.route_details

import com.example.trampled_trails.domain.entity.RouteDomain

interface UpdateRouteDetailsUseCase {
    suspend fun invoke(route: RouteDomain)
}