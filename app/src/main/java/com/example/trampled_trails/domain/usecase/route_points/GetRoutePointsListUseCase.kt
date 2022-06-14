package com.example.trampled_trails.domain.usecase.route_points

import com.example.trampled_trails.domain.entity.PointDomain
import kotlinx.coroutines.flow.Flow

interface GetRoutePointsListUseCase {
    fun invoke(routeId: String): Flow<List<PointDomain>>
}