package com.example.trampled_trails.domain.usecase.route_details

import com.example.trampled_trails.domain.entity.RoutePointsImagesDomain
import kotlinx.coroutines.flow.Flow

interface GetRoutePointsImagesUseCase {
    fun invoke(routeId: String): Flow<List<RoutePointsImagesDomain>>
}