package com.example.gh_coursework.domain.usecase.route

import com.example.gh_coursework.domain.entity.RoutePointDomain
import kotlinx.coroutines.flow.Flow

interface GetRoutePointsUseCase {
    fun invoke(routeId: Int): Flow<List<RoutePointDomain>>
}