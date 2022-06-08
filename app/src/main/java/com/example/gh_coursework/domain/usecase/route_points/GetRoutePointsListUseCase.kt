package com.example.gh_coursework.domain.usecase.route_points

import com.example.gh_coursework.domain.entity.PointDomain
import kotlinx.coroutines.flow.Flow

interface GetRoutePointsListUseCase {
    fun invoke(routeId: String): Flow<List<PointDomain>>
}