package com.example.gh_coursework.domain.usecase.route_details

import com.example.gh_coursework.domain.entity.RoutePointsImagesDomain
import kotlinx.coroutines.flow.Flow

interface GetRoutePointsImagesUseCase {
    fun invoke(routeId: Long): Flow<List<RoutePointsImagesDomain>>
}