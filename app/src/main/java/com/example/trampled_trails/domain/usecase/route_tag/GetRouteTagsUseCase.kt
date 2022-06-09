package com.example.trampled_trails.domain.usecase.route_tag

import com.example.trampled_trails.domain.entity.RouteTagDomain
import kotlinx.coroutines.flow.Flow

interface GetRouteTagsUseCase {
    fun invoke(): Flow<List<RouteTagDomain>>
}