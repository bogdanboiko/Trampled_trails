package com.example.trampled_trails.domain.usecase.route_details

import com.example.trampled_trails.domain.entity.RouteDomain
import kotlinx.coroutines.flow.Flow

interface GetRouteDetailsUseCase {
    fun invoke(routeId: String): Flow<RouteDomain>
}