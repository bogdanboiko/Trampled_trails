package com.example.trampled_trails.domain.usecase.route_tag

import com.example.trampled_trails.domain.entity.RouteTagsDomain

interface AddRouteTagsUseCase {
    suspend fun invoke(tagsList: List<RouteTagsDomain>)
}